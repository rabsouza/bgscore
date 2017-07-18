package br.com.battista.bgscore.service.cache;

import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.text.MessageFormat;

import br.com.battista.bgscore.model.enuns.ActionCacheEnum;

public class EventCache {

    private static final String TAG = EventCache.class.getSimpleName();

    public synchronized static void createEvent(@NonNull ActionCacheEnum... actionsCache) {
        for (ActionCacheEnum actionCache : actionsCache) {
            Log.i(TAG, MessageFormat.format("createEvent: Create new event {0} cache!", actionCache));
            EventBus.getDefault().post(actionCache);
        }
    }
}
