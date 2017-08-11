package br.com.battista.bgscore.model.enuns;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.google.common.collect.Maps;

import java.util.Map;

import br.com.battista.bgscore.R;

public enum FeedbackEnum {

    FEEDBACK_DISSATISFIED(R.drawable.ic_feedback_dissatisfied, R.string.feedback_dissatisfied),
    FEEDBACK_NEUTRAL(R.drawable.ic_feedback_neutral, R.string.feedback_neutral),
    FEEDBACK_SATISFIED(R.drawable.ic_feedback_satisfied, R.string.feedback_satisfied),
    FEEDBACK_VERY_DISSATISFIED(R.drawable.ic_feedback_very_dissatisfied, R.string.feedback_very_dissatisfied),
    FEEDBACK_VERY_SATISFIED(R.drawable.ic_feedback_very_satisfied, R.string.feedback_very_satisfied);

    private static final Map<String, FeedbackEnum> LOOK_UP = Maps.newLinkedHashMap();
    private static final Map<Integer, FeedbackEnum> LOOK_UP_ID = Maps.newLinkedHashMap();

    static {
        for (FeedbackEnum feedback : FeedbackEnum.values()) {
            LOOK_UP.put(feedback.name(), feedback);
            LOOK_UP_ID.put(feedback.getIdResDrawable(), feedback);
        }
    }

    @DrawableRes
    private final int idResDrawable;
    @StringRes
    private final int idResString;

    FeedbackEnum(@DrawableRes int idResDrawable, @StringRes int idResString) {
        this.idResDrawable = idResDrawable;
        this.idResString = idResString;
    }

    public static FeedbackEnum get(String name) {
        FeedbackEnum FeedbackEnum = LOOK_UP.get(name);
        if (FeedbackEnum == null) {
            return FEEDBACK_NEUTRAL;
        }
        return FeedbackEnum;
    }

    public static FeedbackEnum get(@DrawableRes int idResDrawable) {
        FeedbackEnum FeedbackEnum = LOOK_UP_ID.get(idResDrawable);
        if (FeedbackEnum == null) {
            return FEEDBACK_NEUTRAL;
        }
        return FeedbackEnum;
    }

    public int getIdResDrawable() {
        return idResDrawable;
    }

    public int getIdResString() {
        return idResString;
    }

}
