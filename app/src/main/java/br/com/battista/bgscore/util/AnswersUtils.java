package br.com.battista.bgscore.util;

import android.os.Bundle;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.battista.bgscore.MainApplication;

import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_ACTION;
import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_ACTIVITY;
import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_FRAGMENT;
import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_OPEN_ACTIVITY;
import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_OPEN_FRAGMENT;

public class AnswersUtils {

    private AnswersUtils() {
    }

    public static void onActionMetric(String nameAction, String valueAction) {
        Answers.getInstance().logCustom(new CustomEvent(KEY_ACTION)
                .putCustomAttribute(nameAction, valueAction));

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, nameAction);
        bundle.putString(FirebaseAnalytics.Param.VALUE, valueAction);
        MainApplication.instance().getFirebaseAnalytics().logEvent(KEY_ACTION, bundle);
    }

    public static void onOpenActivity(String nameView) {
        Answers.getInstance().logCustom(new CustomEvent(KEY_OPEN_ACTIVITY)
                .putCustomAttribute(KEY_ACTIVITY, nameView));

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(nameView)
                .putContentType(KEY_ACTIVITY));

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, KEY_OPEN_ACTIVITY);
        bundle.putString(FirebaseAnalytics.Param.VALUE, nameView);
        MainApplication.instance().getFirebaseAnalytics().logEvent(KEY_ACTIVITY, bundle);
    }

    public static void onOpenFragment(String nameView) {
        Answers.getInstance().logCustom(new CustomEvent(KEY_OPEN_FRAGMENT)
                .putCustomAttribute(KEY_FRAGMENT, nameView));

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(nameView)
                .putContentType(KEY_FRAGMENT));

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, KEY_OPEN_FRAGMENT);
        bundle.putString(FirebaseAnalytics.Param.VALUE, nameView);
        MainApplication.instance().getFirebaseAnalytics().logEvent(KEY_FRAGMENT, bundle);
    }
}
