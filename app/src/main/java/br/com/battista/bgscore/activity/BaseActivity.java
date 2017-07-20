package br.com.battista.bgscore.activity;

import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_ACTIVITY;
import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_OPEN_ACTIVITY;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.util.AnimationUtils;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private Toolbar toolbar;

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

    protected Context getContext() {
        return this;
    }

    protected Activity getActivity() {
        return this;
    }

    protected void setUpToolbar(int title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            changeTitleToolbar(title);

            Log.d(TAG, "setUpToolbar: Active support toolbar!");
            setSupportActionBar(toolbar);
        }
    }

    protected void changeTitleToolbar(int title) {
        if (title != 0) {
            toolbar.setTitle(title);
        }
    }

    protected void changeTitleCollapsingToolbar(int titleResId) {
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getContext().getString(titleResId));
    }

    protected void setupToolbarDetail() {
        setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            Log.d(TAG, "replaceFragment: Change to fragment!");
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

            transaction.replace(R.id.container, fragment, fragment.getTag());
            transaction.commit();
        }
    }

    protected void replaceDetailFragment(Fragment fragment, int resIdContainer) {
        if (fragment != null) {
            Log.d(TAG, "replaceFragment: Change to detail fragment!");
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            transaction.replace(resIdContainer, fragment, fragment.getTag());
            transaction.commit();
        }
    }

}
