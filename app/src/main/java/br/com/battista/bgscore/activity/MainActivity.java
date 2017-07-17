package br.com.battista.bgscore.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.MessageFormat;

import br.com.battista.bgscore.R;

public class MainActivity extends BaseActivity {

    public static final int DEFAULT_SLEEP_PROGRESS_BAR = 300;
    private static final String TAG = MainActivity.class.getSimpleName();
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
        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, MessageFormat.format("Error load version: {0}", e.getLocalizedMessage()));
        }
        hintVersion.setText(getString(R.string.hint_version, version));
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
            while (currentProgress < MAX_PROGRESS_BAR) {
                SystemClock.sleep(DEFAULT_SLEEP_PROGRESS_BAR);
                currentProgress += incrementProgressBarMain;
                publishProgress(currentProgress);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBarMain.setProgress(currentProgress);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadHomeActivity();
        }
    }
}
