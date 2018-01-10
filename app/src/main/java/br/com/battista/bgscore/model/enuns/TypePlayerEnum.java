package br.com.battista.bgscore.model.enuns;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.util.Map;

public enum TypePlayerEnum {

    PLAYER,
    FRIEND,
    USER;

    private static final Map<String, TypePlayerEnum> LOOK_UP = Maps.newHashMap();

    static {
        for (TypePlayerEnum typePlayer :
                TypePlayerEnum.values()) {
            LOOK_UP.put(typePlayer.name().toUpperCase(), typePlayer);
        }
    }

    public static TypePlayerEnum get(String typePlayer) {
        return LOOK_UP.get(MoreObjects.firstNonNull(typePlayer, PLAYER.name()).toUpperCase());
    }

}
