package br.com.battista.bgscore.model.enuns;

import com.google.common.collect.Maps;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.util.Map;

import br.com.battista.bgscore.R;

public enum AvatarEnum {

    AVATAR_ACKBAR_ADMIRAL(R.drawable.avatar_ackbar_admiral, R.string.avatar_ackbar_admiral),
    AVATAR_BOBA_FETT(R.drawable.avatar_boba_fett, R.string.avatar_boba_fett),
    AVATAR_C3P0(R.drawable.avatar_c3p0, R.string.avatar_c3p0),
    AVATAR_CALRISSIAN_LANDO(R.drawable.avatar_calrissian_lando, R.string.avatar_calrissian_lando),
    AVATAR_CHEWBACCA(R.drawable.avatar_chewbacca, R.string.avatar_chewbacca),
    AVATAR_CLONE_TROOPER(R.drawable.avatar_clone_trooper, R.string.avatar_clone_trooper),
    AVATAR_DARTH_MAUL(R.drawable.avatar_darth_maul, R.string.avatar_darth_maul),
    AVATAR_DARTH_VADER(R.drawable.avatar_darth_vader, R.string.avatar_darth_vader),
    AVATAR_EMPEROR_PALPATINE(R.drawable.avatar_emperor_palpatine, R.string.avatar_emperor_palpatine),
    AVATAR_EWOK(R.drawable.avatar_ewok, R.string.avatar_ewok),
    AVATAR_FETT_JANGO(R.drawable.avatar_fett_jango, R.string.avatar_fett_jango),
    AVATAR_FIVE_RED(R.drawable.avatar_five_red, R.string.avatar_five_red),
    AVATAR_FRIEND(R.drawable.avatar_friend, R.string.avatar_friend),
    AVATAR_WOMAN_1(R.drawable.avatar_woman_1, R.string.avatar_woman),
    AVATAR_WOMAN_2(R.drawable.avatar_woman_2, R.string.avatar_woman),
    AVATAR_WOMAN_3(R.drawable.avatar_woman_3, R.string.avatar_woman),
    AVATAR_MAN_1(R.drawable.avatar_man_1, R.string.avatar_man),
    AVATAR_MAN_2(R.drawable.avatar_man_2, R.string.avatar_man),
    AVATAR_MAN_3(R.drawable.avatar_man_3, R.string.avatar_man),
    AVATAR_GON_JINN_QUI(R.drawable.avatar_gon_jinn_qui, R.string.avatar_gon_jinn_qui),
    AVATAR_GREEDO(R.drawable.avatar_greedo, R.string.avatar_greedo),
    AVATAR_HAN_SOLO(R.drawable.avatar_han_solo, R.string.avatar_han_solo),
    AVATAR_HUTT_JABBA_THE(R.drawable.avatar_hutt_jabba_the, R.string.avatar_hutt_jabba_the),
    AVATAR_JAWA(R.drawable.avatar_jawa, R.string.avatar_jawa),
    AVATAR_KENOBI_OBIWAN(R.drawable.avatar_kenobi_obiwan, R.string.avatar_kenobi_obiwan),
    AVATAR_LEIA_PRINCESS(R.drawable.avatar_leia_princess, R.string.avatar_leia_princess),
    AVATAR_LOBOT(R.drawable.avatar_lobot, R.string.avatar_lobot),
    AVATAR_LUKE_SKYWALKER(R.drawable.avatar_luke_skywalker, R.string.avatar_luke_skywalker),
    AVATAR_PROFILE(R.drawable.avatar_profile, R.string.avatar_profile),
    AVATAR_R2D2(R.drawable.avatar_r2d2, R.string.avatar_r2d2),
    AVATAR_RAIDER_TUSKEN(R.drawable.avatar_raider_tusken, R.string.avatar_raider_tusken),
    AVATAR_STORMTROOPER(R.drawable.avatar_stormtrooper, R.string.avatar_stormtrooper),
    AVATAR_YODA(R.drawable.avatar_yoda, R.string.avatar_yoda);

    @DrawableRes
    private final int idResDrawable;
    @StringRes
    private final int idResString;

    private static final Map<String, AvatarEnum> LOOK_UP = Maps.newLinkedHashMap();
    private static final Map<Integer, AvatarEnum> LOOK_UP_ID = Maps.newLinkedHashMap();

    static {
        for (AvatarEnum avatar : AvatarEnum.values()) {
            LOOK_UP.put(avatar.name(), avatar);
            LOOK_UP_ID.put(avatar.getIdResDrawable(), avatar);
        }
    }

    AvatarEnum(@DrawableRes int idResDrawable, @StringRes int idResString) {
        this.idResDrawable = idResDrawable;
        this.idResString = idResString;
    }

    public int getIdResDrawable() {
        return idResDrawable;
    }

    public int getIdResString() {
        return idResString;
    }

    public static AvatarEnum get(String name) {
        AvatarEnum avatarEnum = LOOK_UP.get(name);
        if (avatarEnum == null) {
            return AVATAR_PROFILE;
        }
        return avatarEnum;
    }

    public static AvatarEnum get(@DrawableRes int idResDrawable) {
        AvatarEnum avatarEnum = LOOK_UP_ID.get(idResDrawable);
        if (avatarEnum == null) {
            return AVATAR_PROFILE;
        }
        return avatarEnum;
    }
}
