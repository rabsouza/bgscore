package br.com.battista.bgscore.constants;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.util.Map;

public enum SharedPreferencesKeys {

    NONE;

    private static final Map<String, SharedPreferencesKeys> LOOK_UP = Maps.newHashMap();

    static {
        for (SharedPreferencesKeys key :
                SharedPreferencesKeys.values()) {
            LOOK_UP.put(key.name().toUpperCase(), key);
        }
    }

    public static SharedPreferencesKeys get(String key) {
        return LOOK_UP.get(MoreObjects.firstNonNull(key, NONE.name()).toUpperCase());
    }

}
