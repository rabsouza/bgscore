package br.com.battista.bgscore.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.fragment.GameFragment;
import br.com.battista.bgscore.fragment.HomeFragment;
import br.com.battista.bgscore.fragment.ProfileFragment;
import br.com.battista.bgscore.fragment.dialog.AboutDialog;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpToolbar(R.string.title_home);
        setUpBottomNavigation();
        processDataActivity(getIntent().getExtras());
    }

    private void processDataActivity(Bundle bundle) {
        Log.d(TAG, "processDataActivity: Process bundle data Activity!");

        if (bundle.containsKey(BundleConstant.NAVIGATION_TO)) {
            int navigationTo = bundle.getInt(BundleConstant.NAVIGATION_TO);

            switch (navigationTo) {
                case BundleConstant.NavigationTo.GAME_FRAGMENT:
                    Log.i(TAG, "loadFragmentInitial: Load the GameFragment!");
                    replaceFragment(GameFragment.newInstance());
                    break;
                default:
                    Log.i(TAG, "loadFragmentInitial: Load the HomeFragment!");
                    replaceFragment(HomeFragment.newInstance());
                    break;
            }

        } else {
            Log.i(TAG, "loadFragmentInitial: Load the HomeFragment!");
            replaceFragment(HomeFragment.newInstance());
        }
    }

    private void setUpBottomNavigation() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
            itemView.setShiftingMode(false);
            itemView.setChecked(false);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                item.setChecked(true);
                                setUpToolbar(R.string.title_home);
                                AnswersUtils.onActionMetric(Actions.ACTION_CLICK_MENU, ValueActions.VALUE_CLICK_MENU_HOME);
                                replaceFragment(HomeFragment.newInstance());
                                break;
                            case R.id.action_matches:
                                item.setChecked(true);
                                AnswersUtils.onActionMetric(Actions.ACTION_CLICK_MENU, ValueActions.VALUE_CLICK_MENU_MATCHES);
                                AndroidUtils.toast(getActivity(), "Em construção!");
                                break;
                            case R.id.action_games:
                                item.setChecked(true);
                                AnswersUtils.onActionMetric(Actions.ACTION_CLICK_MENU, ValueActions.VALUE_CLICK_MENU_GAMES);
                                replaceFragment(GameFragment.newInstance());
                                break;
                            case R.id.action_account:
                                item.setChecked(true);
                                setUpToolbar(R.string.title_profile);
                                AnswersUtils.onActionMetric(Actions.ACTION_CLICK_MENU, ValueActions.VALUE_CLICK_MENU_PROFILE);
                                replaceFragment(ProfileFragment.newInstance());
                                break;
                            case R.id.action_info:
                                AnswersUtils.onActionMetric(Actions.ACTION_CLICK_MENU, ValueActions.VALUE_CLICK_MENU_INFO);
                                AboutDialog.showAbout(getSupportFragmentManager());
                                break;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        dialogCloseActivity();
    }

    private void dialogCloseActivity() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_confirmation_dialog_title_exit)
                .setMessage(R.string.alert_confirmation_dialog_text_exit_app)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                AnswersUtils.onActionMetric(Actions.ACTION_EXIT, ValueActions.VALUE_ACTION_EXIT);
                                finishAffinity();
                            }
                        })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).show();
    }

}
