package br.com.battista.bgscore.service.cache;

import android.support.annotation.NonNull;

import java.text.MessageFormat;

import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.LogUtils;

public class EventCache {

    private static final String TAG = EventCache.class.getSimpleName();

    public synchronized static void createEvent(@NonNull ActionCacheEnum... actionsCache) {
        for (ActionCacheEnum actionCache : actionsCache) {
            LogUtils.i(TAG, MessageFormat.format("createEvent: Create new event {0} cache!", actionCache));
            AndroidUtils.postAction(actionCache);
        }
    }
}
