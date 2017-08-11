package br.com.battista.bgscore.model.enuns;

import com.google.common.collect.Maps;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.util.Map;

import br.com.battista.bgscore.R;

public enum BadgeGameEnum {

    BADGE_GAME_ABSTRACT(R.drawable.badge_game_abstract, R.string.badge_game_abstract),
    BADGE_GAME_AMERITRASH(R.drawable.badge_game_ameritrash, R.string.badge_game_ameritrash),
    BADGE_GAME_EUROS(R.drawable.badge_game_euros, R.string.badge_game_euros),
    BADGE_GAME_FAMILY(R.drawable.badge_game_family, R.string.badge_game_family),
    BADGE_GAME_PARTY(R.drawable.badge_game_party, R.string.badge_game_party),
    BADGE_GAME_WARGAMER(R.drawable.badge_game_wargamer, R.string.badge_game_wargamer),
    BADGE_GAME_NONE(R.drawable.badge_game_none, R.string.badge_game_none);

    private static final Map<String, BadgeGameEnum> LOOK_UP = Maps.newLinkedHashMap();
    private static final Map<Integer, BadgeGameEnum> LOOK_UP_ID = Maps.newLinkedHashMap();

    static {
        for (BadgeGameEnum badgeGame : BadgeGameEnum.values()) {
            LOOK_UP.put(badgeGame.name(), badgeGame);
            LOOK_UP_ID.put(badgeGame.getIdResDrawable(), badgeGame);
        }
    }

    @DrawableRes
    private final int idResDrawable;
    @StringRes
    private final int idResString;

    BadgeGameEnum(@DrawableRes int idResDrawable, @StringRes int idResString) {
        this.idResDrawable = idResDrawable;
        this.idResString = idResString;
    }

    public static BadgeGameEnum get(String name) {
        BadgeGameEnum BadgeGameEnum = LOOK_UP.get(name);
        if (BadgeGameEnum == null) {
            return BADGE_GAME_NONE;
        }
        return BadgeGameEnum;
    }

    public static BadgeGameEnum get(@DrawableRes int idResDrawable) {
        BadgeGameEnum BadgeGameEnum = LOOK_UP_ID.get(idResDrawable);
        if (BadgeGameEnum == null) {
            return BADGE_GAME_NONE;
        }
        return BadgeGameEnum;
    }

    public int getIdResDrawable() {
        return idResDrawable;
    }

    public int getIdResString() {
        return idResString;
    }

}
