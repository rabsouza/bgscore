package br.com.battista.bgscore.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class AndroidUtils {

    private static final String TAG = AndroidUtils.class.getSimpleName();

    private AndroidUtils() {
    }

    public static String getVersionName(@NonNull Activity activity) {
        PackageManager pm = activity.getPackageManager();
        String packageName = activity.getPackageName();
        String versionName;
        try {
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "N/A";
        }

        Log.d(TAG, String.format("getVersionName: Version app: %s!", versionName));
        return versionName;
    }

    public static void changeErrorEditText(@NonNull EditText editText) {
        changeErrorEditText(editText, null, false);
    }

    @Nullable
    public static Boolean isOnline(@NonNull Application application) {
        ConnectivityManager cm =
                (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void changeErrorSpinner(@NonNull MaterialBetterSpinner spinner) {
        changeErrorSpinner(spinner, null, false);
    }

    public static void changeErrorSpinner(@NonNull MaterialBetterSpinner spinner, String msgErro, Boolean error) {
        if (error) {
            Log.e(TAG, msgErro);
            spinner.setError(msgErro);
            spinner.requestFocus();
        } else {
            spinner.setError(null);
        }
    }

    public static void changeErrorEditText(@NonNull EditText editText, String msgErro, Boolean error) {
        if (error) {
            Log.e(TAG, msgErro);
            editText.setError(msgErro);
            editText.requestFocus();
        } else {
            editText.setError(null);
        }
    }

    @Nullable
    public static String getMessageText(@NonNull Context context, int msg) {
        return String.valueOf(context.getText(msg));
    }

    public static void toast(@NonNull Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(@NonNull Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void snackbar(@NonNull View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void snackbar(@NonNull View view, int msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }


}
