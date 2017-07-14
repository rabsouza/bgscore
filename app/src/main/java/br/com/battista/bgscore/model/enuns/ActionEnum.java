package br.com.battista.bgscore.model.enuns;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.util.Map;

public enum ActionEnum {

    NONE,
    START_FRAGMENT_CAMPAIGNS;

    private static final Map<String, ActionEnum> LOOK_UP = Maps.newHashMap();

    static {
        for (ActionEnum action :
                ActionEnum.values()) {
            LOOK_UP.put(action.name().toUpperCase(), action);
        }
    }

    public static ActionEnum get(String typeCard) {
        return LOOK_UP.get(MoreObjects.firstNonNull(typeCard, NONE.name()).toUpperCase());
    }

}
