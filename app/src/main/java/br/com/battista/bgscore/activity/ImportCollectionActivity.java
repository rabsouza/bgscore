package br.com.battista.bgscore.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.fragment.collection.NewImportCollectionFragment;
import br.com.battista.bgscore.util.AnswersUtils;

public class ImportCollectionActivity extends BaseActivity {

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
}
