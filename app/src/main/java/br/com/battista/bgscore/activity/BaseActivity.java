package br.com.battista.bgscore.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.util.AnimationActivityUtils;
import br.com.battista.bgscore.util.LogUtils;

import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_ACTIVITY;
import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_OPEN_ACTIVITY;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

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
            getWindow().setEnterTransition(AnimationActivityUtils.makeEnterTransition());
        }
    }

    protected Context getContext() {
        return this;
    }

    protected Activity getActivity() {
        return this;
    }

    protected void setUpToolbar(@StringRes int title) {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            changeTitleToolbar(title);

            LogUtils.d(TAG, "setUpToolbar: Active support toolbar!");
            setSupportActionBar(toolbar);
        }
    }

    protected void changeTitleToolbar(@StringRes int idTitle) {
        toolbar.setTitle(idTitle);
    }

    protected void changeActionActive(@IdRes int idAction) {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(idAction);
    }

    protected void changeTitleCollapsingToolbar(@StringRes int titleResId) {
        CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getContext().getString(titleResId));
    }

    protected void setupToolbarDetail() {
        setSupportActionBar(findViewById(R.id.detail_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            LogUtils.d(TAG, "replaceFragment: Change to fragment!");
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

            transaction.replace(R.id.container, fragment, fragment.getTag());
            transaction.commit();
        }
    }

    protected void replaceDetailFragment(Fragment fragment, int resIdContainer) {
        if (fragment != null) {
            LogUtils.d(TAG, "replaceFragment: Change to detail fragment!");
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            transaction.replace(resIdContainer, fragment, fragment.getTag());
            transaction.commit();
        }
    }

}
