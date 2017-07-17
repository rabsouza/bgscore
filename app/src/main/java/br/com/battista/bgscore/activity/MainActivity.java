package br.com.battista.bgscore.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ProgressBar;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.util.AndroidUtils;

public class MainActivity extends BaseActivity {

    private static final int DEFAULT_STEPS_PROGRESS_BAR = 5;
    private static final int MAX_PROGRESS_BAR = 100;
    public static final int DEFAULT_SLEEP_PROGRESS_BAR = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processProgressBarMain();
    }

    private void processProgressBarMain() {
        final ProgressBar progressBarMain = (ProgressBar) findViewById(R.id.progress_bar_main);

        new ProgressBarAsyncTask(progressBarMain).execute();
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

            AndroidUtils.toast(getActivity(), "Acabou!");
        }
    }
}
