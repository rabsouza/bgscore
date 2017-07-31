package br.com.battista.bgscore.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.fragment.match.NewMatchFragment;
import br.com.battista.bgscore.model.Match;

public class MatchActivity extends BaseActivity {

    private static final String TAG = MatchActivity.class.getSimpleName();

    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        setupToolbarDetail();
        changeTitleCollapsingToolbar(R.string.title_add_match);
        processDataActivity(getIntent().getExtras());
    }

    private void processDataActivity(Bundle bundle) {
        Log.d(TAG, "processDataActivity: Process bundle data Activity!");
        if (bundle.containsKey(BundleConstant.DATA)) {
            match = (Match) bundle.getSerializable(BundleConstant.DATA);
            match.reloadId();

            Log.i(TAG, "loadFragmentInitial: Load the NewMatchFragment!");
            replaceDetailFragment(NewMatchFragment.newInstance(match), R.id.detail_container);
        } else {
            Log.i(TAG, "loadFragmentInitial: Load the NewMatchFragment!");
            replaceDetailFragment(NewMatchFragment.newInstance(null), R.id.detail_container);
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
