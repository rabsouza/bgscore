package br.com.battista.bgscore.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.service.Inject;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;

public class MainActivity extends BaseActivity {

    public static final int DEFAULT_SLEEP_PROGRESS_BAR = 50;
    private static final int DEFAULT_STEPS_PROGRESS_BAR = 5;
    private static final int MAX_PROGRESS_BAR = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processProgressBarMain();
        loadVersionName();
    }

    private void loadVersionName() {
        final TextView hintVersion = (TextView) findViewById(R.id.hint_version);
        hintVersion.setText(getString(R.string.hint_version, AndroidUtils.getVersionName(this)));
    }

    private void processProgressBarMain() {
        final ProgressBar progressBarMain = (ProgressBar) findViewById(R.id.progress_bar_main);
        new ProgressBarAsyncTask(progressBarMain).execute();
    }

    private void loadHomeActivity() {
        Bundle args = new Bundle();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtras(args);

        getContext().startActivity(intent);
    }

    private class ProgressBarAsyncTask extends AsyncTask<Void, Integer, Void> {
        private final ProgressBar progressBarMain;
        int currentProgress;
        int incrementProgressBarMain;

        ProgressBarAsyncTask(ProgressBar progressBarMain) {
            this.progressBarMain = progressBarMain;
            currentProgress = 0;
            incrementProgressBarMain = MAX_PROGRESS_BAR / DEFAULT_STEPS_PROGRESS_BAR;
        }

        @Override
        protected void onPreExecute() {
            progressBarMain.setProgress(currentProgress);
            progressBarMain.setMax(MAX_PROGRESS_BAR);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            step01CheckUser();

            while (currentProgress < MAX_PROGRESS_BAR) {
                SystemClock.sleep(DEFAULT_SLEEP_PROGRESS_BAR);
                currentProgress += incrementProgressBarMain;
                publishProgress(currentProgress);
            }
            return null;
        }

        private void step01CheckUser() {
            MainApplication instance = MainApplication.instance();
            User user = instance.getUser();
            if (user == null) {
                user = new User().username(getString(R.string.text_default_username));
                user.initEntity();
                instance.setUser(user);
                currentProgress += incrementProgressBarMain;
                publishProgress(currentProgress);
            } else {
                Inject.provideCacheManageService().reloadSyncAllDataCache();
                currentProgress += incrementProgressBarMain;
                publishProgress(currentProgress);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBarMain.setProgress(currentProgress);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AnswersUtils.onActionMetric(Actions.ACTION_OPEN, ValueActions.VALUE_ACTION_OPEN);
            loadHomeActivity();
        }
    }
}
