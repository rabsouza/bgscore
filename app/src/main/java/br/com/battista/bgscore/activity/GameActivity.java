package br.com.battista.bgscore.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.fragment.game.NewGameFragment;

public class GameActivity extends BaseActivity {

    private static final String TAG = GameActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        setupToolbarDetail();
        changeTitleCollapsingToolbar(R.string.title_add_game);

        processDataActivity(getIntent().getExtras());
    }

    private void processDataActivity(Bundle bundle) {
        Log.d(TAG, "processDataActivity: Process bundle data Activity!");

        Log.i(TAG, "loadFragmentInitial: Load the NewGameFragment!");
        replaceDetailFragment(NewGameFragment.newInstance(), R.id.detail_container);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                .setPositiveButton(R.string.btn_confirmation_dialog_exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        superOnBackPressed();
                    }

                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).show();
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }
}
