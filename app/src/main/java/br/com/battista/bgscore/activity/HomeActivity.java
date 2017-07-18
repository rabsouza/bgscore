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
import br.com.battista.bgscore.fragment.dialog.AboutDialog;
import br.com.battista.bgscore.fragment.HomeFragment;
import br.com.battista.bgscore.util.AndroidUtils;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpToolbar(R.string.title_home);
        setUpBottomNavigation();

        Log.i(TAG, "loadFragmentInitial: Load the HomeFragment!");
        replaceFragment(HomeFragment.newInstance());
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
                                AndroidUtils.toast(getActivity(), "Clicou em Home!");
                                break;
                            case R.id.action_games:
                                item.setChecked(true);
                                AndroidUtils.toast(getActivity(), "Clicou em Partidas!");
                                break;
                            case R.id.action_account:
                                item.setChecked(true);
                                AndroidUtils.toast(getActivity(), "Clicou em Perfil!");
                                break;
                            case R.id.action_info:
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
                .setMessage(R.string.alert_confirmation_dialog_text_exit)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_exit,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finishAffinity();
                            }
                        })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).show();
    }

}
