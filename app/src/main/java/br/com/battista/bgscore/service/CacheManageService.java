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

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;

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
        MainApplication instance = MainApplication.instance();
        Log.i(TAG, MessageFormat.format("onActionCache: No process to action: {0}.", action));
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onCreate: Unregister event bus to Action!");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
