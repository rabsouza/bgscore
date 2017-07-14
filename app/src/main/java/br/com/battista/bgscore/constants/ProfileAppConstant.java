package br.com.battista.bgscore.constants;

import com.google.common.collect.Maps;

import java.util.Map;

public enum ProfileAppConstant {

    ADMIN,
    FRIEND,
    APP;

    private static final Map<String, ProfileAppConstant> LOOK_UP = Maps.newHashMap();

    static {
        for (ProfileAppConstant profile :
                ProfileAppConstant.values()) {
            LOOK_UP.put(profile.name().toUpperCase(), profile);
        }
    }

    public static ProfileAppConstant get(String name) {
        return LOOK_UP.get(name);
    }
}
