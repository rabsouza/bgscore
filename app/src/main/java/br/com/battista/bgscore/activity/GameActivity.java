package br.com.battista.bgscore.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.fragment.game.NewGameFragment;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.util.AnswersUtils;

public class GameActivity extends BaseActivity {

    private static final String TAG = GameActivity.class.getSimpleName();

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        setupToolbarDetail();
        changeTitleCollapsingToolbar(R.string.title_game);

        processDataActivity(getIntent().getExtras());
    }

    private void processDataActivity(Bundle bundle) {
        Log.d(TAG, "processDataActivity: Process bundle data Activity!");
        if (bundle.containsKey(BundleConstant.DATA)) {
            game = (Game) bundle.getSerializable(BundleConstant.DATA);
            game.reloadId();

            Log.i(TAG, "loadFragmentInitial: Load the NewGameFragment!");
            replaceDetailFragment(NewGameFragment.newInstance(game), R.id.detail_container);
        } else {
            Log.i(TAG, "loadFragmentInitial: Load the NewGameFragment!");
            replaceDetailFragment(NewGameFragment.newInstance(null), R.id.detail_container);
        }
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
        AnswersUtils.onActionMetric(Actions.ACTION_BACK, ValueActions.VALUE_BACK_GAME);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alert_confirmation_dialog_title_exit)
                .setMessage(R.string.alert_confirmation_dialog_text_exit)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        superOnBackPressed();
                    }

                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
        alertDialog.show();
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }
}
