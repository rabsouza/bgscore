package br.com.battista.bgscore.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import br.com.battista.bgscore.util.AnimationUtils;

import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_ACTIVITY;
import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_OPEN_ACTIVITY;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String nameView = this.getClass().getSimpleName();

        Answers.getInstance().logCustom(new CustomEvent(KEY_OPEN_ACTIVITY)
                .putCustomAttribute(KEY_ACTIVITY, nameView));

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(nameView)
                .putContentType(KEY_ACTIVITY));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(AnimationUtils.makeEnterTransition());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected Context getContext() {
        return this;
    }

    protected Activity getActivity() {
        return this;
    }

}
