package br.com.battista.bgscore.custom;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.text.MessageFormat;

public class ProgressApp extends AsyncTask<Void, Integer, Boolean> {

    public static final String TAG = ProgressApp.class.getSimpleName();

    private ProgressDialog progress;
    private Integer currProgress = 0;
    private Integer maxProgress = 100;
    private Integer offsetProgress = 10;
    private Integer timeSleep = 1000;
    private Activity activity;
    private Boolean styleProgress = Boolean.TRUE;

    public ProgressApp(Activity activity, Integer messageResId) {
        this.activity = activity;
        createNewProgressDialog(activity, messageResId);
    }

    public ProgressApp(Activity activity, Integer messageResId, Boolean styleProgress) {
        this.activity = activity;
        this.styleProgress = styleProgress;
        createNewProgressDialog(activity, messageResId);
    }

    public ProgressApp(Activity activity, Integer messageResId, Integer currProgress,
                       Integer maxProgress, Integer offsetProgress, Integer timeSleep,
                       Boolean styleProgress) {
        this.currProgress = currProgress;
        this.maxProgress = maxProgress;
        this.offsetProgress = offsetProgress;
        this.timeSleep = timeSleep;
        this.activity = activity;
        this.styleProgress = styleProgress;

        createNewProgressDialog(activity, messageResId);
    }

    private void createNewProgressDialog(Activity activity, Integer messageResId) {
        if (activity == null) {
            String detailMessage = "Activity don't null!";
            Log.e(TAG, detailMessage);
            throw new RuntimeException(detailMessage);
        }

        progress = new ProgressDialog(activity);
        progress.setCancelable(false);
        if (styleProgress) {
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        } else {
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progress.setIndeterminate(false);
        progress.setProgress(currProgress);
        progress.setMax(maxProgress);
        String message = null;
        if (messageResId != null) {
            message = activity.getResources().getString(messageResId);
            progress.setMessage(message);
        }
        progress.setIcon(android.R.drawable.ic_dialog_info);

        Log.i(TAG,
                MessageFormat.format("New progress dialog [progress:{0}, max:{1}, offset:{2}, message:{3}, activity:{4}]",
                        currProgress, maxProgress, offsetProgress, message, activity.getTitle()));
    }

    public ProgressDialog getProgress() {
        return progress;
    }

    public ProgressApp withCurrProgress(final Integer currProgress) {
        this.currProgress = currProgress;
        return this;
    }

    public ProgressApp withMaxProgress(final Integer maxProgress) {
        this.maxProgress = maxProgress;
        return this;
    }

    public ProgressApp withOffsetProgress(final Integer offsetProgress) {
        this.offsetProgress = offsetProgress;
        return this;
    }

    public ProgressApp withTimeSleep(final Integer timeSleep) {
        this.timeSleep = timeSleep;
        return this;
    }

    public void dismissProgress() {
        if (activity == null || activity.isFinishing()) {
            progress = null;
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            progress = null;
            return;
        }

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    protected void onPreExecute() {
        progress.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            while (currProgress <= maxProgress) {
                currProgress += offsetProgress;
                Thread.sleep(timeSleep);
                publishProgress(currProgress);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progress.setProgress(currProgress);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (!result) {
            Toast.makeText(activity,
                    String.format("Erro on create activity: %s", activity.getTitle()),
                    Toast.LENGTH_LONG).show();
            activity.finish();
        }
        dismissProgress();
    }

}
