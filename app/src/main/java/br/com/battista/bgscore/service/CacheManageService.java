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
import br.com.battista.bgscore.model.enuns.ActionDatabaseEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.util.AndroidUtils;

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
        AndroidUtils.postAction(ActionCacheEnum.LOAD_ALL_DATA);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onActionCache(ActionCacheEnum action) {
        Log.i(TAG, MessageFormat.format("onActionCache: Process to action: {0}.", action));
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();

        if (ActionCacheEnum.LOAD_DATA_GAME.equals(action)) {
            loadAllDataRankingGamesAddToCache(user);
            loadAllDataGameAddToCache(user);
            if (user.isAutomaticBackup()) {
                AndroidUtils.postAction(ActionDatabaseEnum.BACKUP_ALL_DATA);
            }
        } else if (ActionCacheEnum.LOAD_DATA_MATCHES.equals(action)) {
            loadAllDataMatchAddToCache(user);
            loadAllDataRankingGamesAddToCache(user);
            loadAllDataGameAddToCache(user);
            if (user.isAutomaticBackup()) {
                AndroidUtils.postAction(ActionDatabaseEnum.BACKUP_ALL_DATA);
            }
        } else if (ActionCacheEnum.LOAD_DATA_RANKING_GAMES.equals(action)) {
            loadAllDataRankingGamesAddToCache(user);
        } else if (ActionCacheEnum.LOAD_ALL_DATA.equals(action)) {
            loadAllDataMatchAddToCache(user);
            loadAllDataRankingGamesAddToCache(user);
            loadAllDataGameAddToCache(user);
        }

        instance.setUser(user);
    }

    private synchronized void loadAllDataMatchAddToCache(User user) {
        Log.i(TAG, "loadAllDataMatchAddToCache: Update data match user!");
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
    }

    private synchronized void loadAllDataRankingGamesAddToCache(User user) {
        Log.i(TAG, "loadAllDataRankingGamesAddToCache: Update data racking user!");
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
    }

    private synchronized void loadAllDataGameAddToCache(User user) {
        Log.i(TAG, "loadAllDataGameAddToCache: Update data game user!");
        user.setNumGames(user.getRankingGames().size());
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onCreate: Unregister event bus to Action!");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
