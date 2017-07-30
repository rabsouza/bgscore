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
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;

public class CacheManageService extends Service {

    public static final Boolean CACHED = Boolean.FALSE;
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onActionCache(ActionCacheEnum action) {
        final MainApplication instance = MainApplication.instance();

        Log.i(TAG, MessageFormat.format("onActionCache: Process to action: {0}.", action));
        if (ActionCacheEnum.LOAD_DATA_GAME.equals(action)) {
            loadAllDataGameAddToCache(instance);
        } else if (ActionCacheEnum.LOAD_DATA_MATCHES.equals(action)) {
            loadAllDataMatchAddToCache(instance);
        }
    }

    private void loadAllDataMatchAddToCache(MainApplication instance) {
        User user = instance.getUser();
        Log.i(TAG, "loadAllDataMatchAddToCache: Find all data in DB!");
        final MatchRepository matchRepository = new MatchRepository();
        final List<Match> matches = matchRepository.findAll();
        user.setNumMatches(matches.size());
        if (matches.size() == 0) {
            user.setLastPlay(null);
        }

        long totalTime = 0;
        for (Match match : matches) {
            totalTime += match.getDuration();
        }
        user.setTotalTime(totalTime);
        instance.setUser(user);
    }

    private void loadAllDataGameAddToCache(MainApplication instance) {
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
