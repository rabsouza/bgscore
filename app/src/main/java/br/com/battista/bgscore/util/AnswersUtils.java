package br.com.battista.bgscore.util;

import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_ACTION;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

public class AnswersUtils {

    private AnswersUtils() {
    }

    public static void onActionMetric(String nameAction, String valueAction) {
        Answers.getInstance().logCustom(new CustomEvent(KEY_ACTION)
                .putCustomAttribute(nameAction, valueAction));
    }
}
