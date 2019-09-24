package br.com.battista.bgscore.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.fragment.collection.NewImportCollectionFragment;
import br.com.battista.bgscore.fragment.game.NewGameFragment;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.LogUtils;

public class ImportCollectionActivity extends BaseActivity {

    private static final String TAG = ImportCollectionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_collection);

        setupToolbarDetail();
        changeTitleCollapsingToolbar(R.string.title_import_collection);

        replaceDetailFragment(NewImportCollectionFragment.newInstance(), R.id.detail_container);
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
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_BACK, CrashlyticsConstant.ValueActions.VALUE_BACK_IMPORT_COLLECTION);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alert_confirmation_dialog_title_exit)
                .setMessage(R.string.alert_confirmation_dialog_text_exit)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_exit, (dialog, whichButton) -> superOnBackPressed())
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
        alertDialog.show();
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }
}
