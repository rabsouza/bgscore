package br.com.battista.bgscore.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.RankingGamesDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;

public class CacheManageService extends Service {

    private static final String TAG = CacheManageService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Register event bus to Action!");
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void reloadAllDataCache() {
        Log.i(TAG, "reloadAllDataCache: Reload all data cache!!!");
        EventBus.getDefault().post(ActionCacheEnum.LOAD_DATA_GAME);
        EventBus.getDefault().post(ActionCacheEnum.LOAD_DATA_MATCHES);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onActionCache(ActionCacheEnum action) {
        Log.i(TAG, MessageFormat.format("onActionCache: Process to action: {0}.", action));
        if (ActionCacheEnum.LOAD_DATA_GAME.equals(action)) {
            loadAllDataGameAddToCache();
            loadAllDataRankingGamesAddToCache();
        } else if (ActionCacheEnum.LOAD_DATA_MATCHES.equals(action)) {
            loadAllDataMatchAddToCache();
            loadAllDataRankingGamesAddToCache();
        } else if (ActionCacheEnum.LOAD_DATA_RANKING_GAMES.equals(action)) {
            loadAllDataRankingGamesAddToCache();
        }
    }

    private void loadAllDataMatchAddToCache() {
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();
        Log.i(TAG, "loadAllDataMatchAddToCache: Find all data in DB!");
        final MatchRepository matchRepository = new MatchRepository();
        final List<Match> matches = matchRepository.findAll();
        user.setNumMatches(matches.size());
        if (matches.size() == 0) {
            user.lastPlayed(null);
        } else {
            Match lastMatch = matches.get(0);
            user.lastPlayed(lastMatch.getCreatedAt());
        }

        long totalTime = 0;
        for (Match match : matches) {
            totalTime += match.getDuration();
        }
        user.setTotalTime(totalTime);
        instance.setUser(user);
    }

    private void loadAllDataRankingGamesAddToCache() {
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();

        Log.i(TAG, "loadAllDataRankingGamesAddToCache: Find all data in DB!");
        final List<Game> games = new GameRepository().findAll();
        user.clearRankingGames();
        for (Game game : games) {

            final List<Match> matches = new MatchRepository().findByGameId(game.getId());
            if (!matches.isEmpty()) {

                Match lastMatch = matches.get(0);
                long durationTotal = 0;
                for (Match match : matches) {
                    durationTotal += match.getDuration();
                }
                final RankingGamesDto rankingGames = new RankingGamesDto()
                        .count(matches.size())
                        .lastPlayed(lastMatch.getCreatedAt())
                        .game(game)
                        .duration(durationTotal);
                user.addRankingGames(rankingGames);
            }
        }

        instance.setUser(user);
    }

    private void loadAllDataGameAddToCache() {
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();
        Log.i(TAG, "loadAllDataGameAddToCache: Find all data in DB!");
        final GameRepository cardRepository = new GameRepository();
        final List<Game> games = cardRepository.findAll();
        user.setNumGames(games.size());
        instance.setUser(user);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onCreate: Unregister event bus to Action!");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
