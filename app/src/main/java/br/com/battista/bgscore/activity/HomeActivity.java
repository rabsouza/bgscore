package br.com.battista.bgscore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import br.com.battista.bgscore.BuildConfig;
import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.fragment.GameFragment;
import br.com.battista.bgscore.fragment.HomeFragment;
import br.com.battista.bgscore.fragment.MatchFragment;
import br.com.battista.bgscore.fragment.ProfileFragment;
import br.com.battista.bgscore.fragment.dialog.WelcomeDialog;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.LogUtils;

import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.FINISH_MATCH_FRAGMENT;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpToolbar(R.string.title_home);
        setUpBottomNavigation();
        processDataActivity(getIntent().getExtras());

        final User user = MainApplication.instance().getUser();
        if (user.isWelcome() || !user.getLastBuildVersion().equals(BuildConfig.VERSION_CODE)) {
            WelcomeDialog.newInstance(0).showAbout(getSupportFragmentManager());
        }
    }

    private void processDataActivity(Bundle bundle) {
        LogUtils.d(TAG, "processDataActivity: Process bundle data Activity!");

        if (bundle != null && bundle.containsKey(BundleConstant.NAVIGATION_TO)) {
            int navigationTo = bundle.getInt(BundleConstant.NAVIGATION_TO);

            switch (navigationTo) {
                case BundleConstant.NavigationTo.GAME_FRAGMENT:
                    LogUtils.i(TAG, "loadFragmentInitial: Load the GameFragment!");
                    changeActionActive(R.id.action_games);
                    replaceFragment(GameFragment.newInstance());
                    break;
                case BundleConstant.NavigationTo.MATCH_FRAGMENT:
                    LogUtils.i(TAG, "loadFragmentInitial: Load the MatchFragment!");
                    changeActionActive(R.id.action_matches);
                    replaceFragment(MatchFragment.newInstance());
                    break;
                case BundleConstant.NavigationTo.FINISH_MATCH_FRAGMENT:
                    LogUtils.i(TAG, "loadFragmentInitial: Start finish match!");
                    Match match = (Match) bundle.get(BundleConstant.DATA);
                    Bundle args = new Bundle();
                    Intent intent = new Intent(getContext(), MatchActivity.class);
                    args.putSerializable(BundleConstant.DATA, match);
                    args.putInt(NAVIGATION_TO, FINISH_MATCH_FRAGMENT);
                    intent.putExtras(args);

                    getContext().startActivity(intent);
                default:
                    LogUtils.i(TAG, "loadFragmentInitial: Load the HomeFragment!");
                    changeActionActive(R.id.action_home);
                    replaceFragment(HomeFragment.newInstance());
                    break;
            }

        } else {
            LogUtils.i(TAG, "loadFragmentInitial: Load the HomeFragment!");
            replaceFragment(HomeFragment.newInstance());
        }
    }

    @SuppressLint("RestrictedApi")
    private void setUpBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        ((BottomNavigationItemView) menuView.getChildAt(0)).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            item.setChecked(true);
                            setUpToolbar(R.string.title_home);
                            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_MENU,
                                    ValueActions.VALUE_CLICK_MENU_HOME);
                            replaceFragment(HomeFragment.newInstance());
                            break;
                        case R.id.action_matches:
                            item.setChecked(true);
                            setUpToolbar(R.string.title_matches);
                            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_MENU,
                                    ValueActions.VALUE_CLICK_MENU_MATCHES);
                            replaceFragment(MatchFragment.newInstance());
                            break;
                        case R.id.action_games:
                            item.setChecked(true);
                            setUpToolbar(R.string.title_games);
                            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_MENU,
                                    ValueActions.VALUE_CLICK_MENU_GAMES);
                            replaceFragment(GameFragment.newInstance());
                            break;
                        case R.id.action_more:
                            item.setChecked(true);
                            setUpToolbar(R.string.title_account);
                            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_MENU,
                                    ValueActions.VALUE_CLICK_MENU_PROFILE);
                            replaceFragment(ProfileFragment.newInstance());
                            break;
                    }
                    return false;
                });
    }

    @Override
    public void onBackPressed() {
        AnswersUtils.onActionMetric(Actions.ACTION_EXIT,
                ValueActions.VALUE_ACTION_EXIT);
        finishAffinity();
    }

}
