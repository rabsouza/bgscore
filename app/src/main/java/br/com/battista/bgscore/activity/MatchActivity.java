package br.com.battista.bgscore.activity;

import static br.com.battista.bgscore.constants.BundleConstant.DATA;
import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant.NavigationTo;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.fragment.match.DetailMatchFragment;
import br.com.battista.bgscore.fragment.match.FinishMatchFragment;
import br.com.battista.bgscore.fragment.match.NewMatchFragment;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.LogUtils;

public class MatchActivity extends BaseActivity {

    private static final String TAG = MatchActivity.class.getSimpleName();

    private Match match;
    private boolean isDetailMatch = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        setupToolbarDetail();
        changeTitleCollapsingToolbar(R.string.title_match);
        processDataActivity(getIntent().getExtras());
    }

    private void processDataActivity(Bundle bundle) {
        LogUtils.d(TAG, "processDataActivity: Process bundle data Activity!");
        if (bundle != null && bundle.containsKey(DATA)) {
            match = (Match) bundle.getSerializable(DATA);
            match.reloadId();

            if (bundle != null && bundle.containsKey(NAVIGATION_TO)) {
                int navigationTo = bundle.getInt(NAVIGATION_TO, NavigationTo.NEW_MATCH_FRAGMENT);
                switch (navigationTo) {
                    case NavigationTo.NEW_MATCH_FRAGMENT:
                        LogUtils.i(TAG, "loadFragmentInitial: Load the NewMatchFragment!");
                        isDetailMatch = false;
                        replaceDetailFragment(NewMatchFragment.newInstance(match),
                                R.id.detail_container);
                        break;
                    case NavigationTo.FINISH_MATCH_FRAGMENT:
                        LogUtils.i(TAG, "loadFragmentInitial: Load the FinishMatchFragment!");
                        isDetailMatch = false;
                        replaceDetailFragment(FinishMatchFragment.newInstance(match),
                                R.id.detail_container);
                        break;
                    case NavigationTo.DETAIL_MATCH_FRAGMENT:
                        LogUtils.i(TAG, "loadFragmentInitial: Load the DetailMatchFragment!");
                        isDetailMatch = true;
                        replaceDetailFragment(DetailMatchFragment.newInstance(match),
                                R.id.detail_container);
                        break;
                }
                return;
            }
        } else {
            match = null;
        }
        LogUtils.i(TAG, "loadFragmentInitial: Load the NewMatchFragment!");
        replaceDetailFragment(NewMatchFragment.newInstance(match), R.id.detail_container);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isDetailMatch) {
            superOnBackPressed();
        } else {
            dialogCloseActivity();
        }
    }

    private void dialogCloseActivity() {
        AnswersUtils.onActionMetric(Actions.ACTION_BACK, ValueActions.VALUE_BACK_MATCH);

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
