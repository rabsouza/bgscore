package br.com.battista.bgscore.service;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.ActionCollection;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.model.enuns.ActionCollectionEnum;
import br.com.battista.bgscore.model.enuns.ActionDatabaseEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.LogUtils;

public class CollectionManageService {

    private static final String TAG = CollectionManageService.class.getSimpleName();
    private static CollectionManageService instance;

    public static CollectionManageService getInstance() {
        if (instance == null) {
            instance = new CollectionManageService();
        }
        return instance;
    }

    public void onCreate() {
        LogUtils.i(TAG, "onCreate: Register event bus to Action!");
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onActionCache(@NonNull ActionCollection actionCollection){
        
        ActionCollectionEnum action = actionCollection.getAction();
        List<Game> gamesOwn = actionCollection.getGamesOwn();
        List<Game> gamesWishlist = actionCollection.getGamesWishlist();
        List<Game> gamesPlayed = actionCollection.getGamesPlayed();

        LogUtils.i(TAG, MessageFormat.format(
                "onActionCache: Process to action: {0} with collections [own={1}, wishlist={2}, played={3}].",
                action, gamesOwn.size(), gamesWishlist.size(), gamesPlayed.size()));
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();

        if (ActionCollectionEnum.LOAD_BGG_COLLECTION.equals(action)) {

            processGamesOwn(gamesOwn);
            processGamesWishlist(gamesWishlist);
            processGamesPlayed(gamesPlayed);

            AndroidUtils.postAction(ActionCacheEnum.LOAD_DATA_GAME);
            if (user.isAutomaticBackup()) {
                AndroidUtils.postAction(ActionDatabaseEnum.BACKUP_ALL_DATA);
            }
        }
    }

    private void processGamesOwn(List<Game> gamesOwn) {
        LogUtils.d(TAG, MessageFormat.format(
                "processGamesOwn: Process all data collection own games [{0}]!", gamesOwn.size()));

        final GameRepository gameRepository = new GameRepository();
        Map<Long, Game> mapGames = createCacheGame();

        for (Game gameOwn: gamesOwn) {
            if(mapGames.containsKey(gameOwn.getIdBGG())){
                final Game game = mapGames.get(gameOwn.getIdBGG());
                game.myGame(Boolean.TRUE);
                game.update();
                gameRepository.save(game);
            }else{
                gameOwn.update();
                gameOwn.myGame(Boolean.TRUE);
                gameRepository.save(gameOwn);
            }
        }
    }

    private void processGamesWishlist(List<Game> gamesWishlist) {
        LogUtils.d(TAG, MessageFormat.format(
                "processGamesOwn: Process all data collection wishlist games [{0}]!", gamesWishlist.size()));

        final GameRepository gameRepository = new GameRepository();
        Map<Long, Game> mapGames = createCacheGame();

        for (Game gameWishlist: gamesWishlist) {
            if(mapGames.containsKey(gameWishlist.getIdBGG())){
                final Game game = mapGames.get(gameWishlist.getIdBGG());
                game.wantGame(Boolean.TRUE);
                game.update();
                gameRepository.save(game);
            }else{
                gameWishlist.update();
                gameWishlist.wantGame(Boolean.TRUE);
                gameRepository.save(gameWishlist);
            }
        }
    }

    private void processGamesPlayed(List<Game> gamesPlayed) {
        LogUtils.d(TAG, MessageFormat.format(
                "processGamesOwn: Process all data collection played games [{0}]!", gamesPlayed.size()));

        final GameRepository gameRepository = new GameRepository();
        Map<Long, Game> mapGames = createCacheGame();

        for (Game gamePlayed: gamesPlayed) {
            if(mapGames.containsKey(gamePlayed.getIdBGG())){
                final Game game = mapGames.get(gamePlayed.getIdBGG());
                game.update();
                gameRepository.save(game);
            }else{
                gamePlayed.update();
                gameRepository.save(gamePlayed);
            }
        }
    }

    @NonNull
    private synchronized Map<Long, Game> createCacheGame() {
        Map<Long, Game> mapGames = new HashMap();
        final List<Game> games = new GameRepository().findAll();
        for (Game game: games) {
            if(game.getIdBGG() != null && game.getIdBGG() >0) {
                mapGames.put(game.getIdBGG(), game);
            }
        }
        return mapGames;
    }

    public void onDestroy() {
        LogUtils.i(TAG, "onCreate: Unregister event bus to Action!");
        EventBus.getDefault().unregister(this);
    }
}
