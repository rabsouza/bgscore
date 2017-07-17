package br.com.battista.bgscore.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.dialog.AboutDialog;
import br.com.battista.bgscore.util.AndroidUtils;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpToolbar(R.string.title_home);
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                AndroidUtils.toast(getActivity(), "Clicou em Home!");
                                break;
                            case R.id.action_games:
                                AndroidUtils.toast(getActivity(), "Clicou em Partidas!");
                                break;
                            case R.id.action_account:
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
