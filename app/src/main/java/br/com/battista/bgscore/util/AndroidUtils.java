package br.com.battista.bgscore.util;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Random;

import br.com.battista.bgscore.R;

public class AndroidUtils {

    private static final String TAG = AndroidUtils.class.getSimpleName();

    private AndroidUtils() {
    }

    @NonNull
    public static PopupWindow createPopupWindow(@NonNull Context context, @LayoutRes int resourcePopupWindow) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(resourcePopupWindow, null);
        final PopupWindow popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.animationPopup);
        return popupWindow;
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

        LogUtils.d(TAG, String.format("getVersionName: Version app: %s!", versionName));
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

    public static void changeErrorSpinner(@NonNull MaterialBetterSpinner spinner,
                                          String msgError, Boolean error) {
        if (error) {
            LogUtils.e(TAG, msgError);
            spinner.setError(msgError);
            spinner.requestFocus();
        } else {
            spinner.setError(null);
        }
    }

    public static void changeErrorEditText(@NonNull EditText editText, String msgError, Boolean error) {
        if (error) {
            LogUtils.e(TAG, msgError);
            editText.setError(msgError);
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
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void toast(@NonNull Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void toastLong(@NonNull Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void snackbar(@NonNull View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void snackbar(@NonNull View view, int msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void postAction(@NonNull Object action) {
        EventBus.getDefault().post(action);
    }

    public static File getFileDir(@NonNull Context context) {
        File dirBackup = Environment.getExternalStorageDirectory();
        if (dirBackup == null || !isExternalStorageReadable()) {
            dirBackup = context.getExternalFilesDir(null);
            if (dirBackup == null) {
                dirBackup = context.getFilesDir();
            }
        }
        return dirBackup;
    }

    public static void checkPermissions(Activity activity, String[] storage_permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(activity, storage_permissions, 0);
                } else {
                    ActivityCompat.requestPermissions(activity, storage_permissions, 0);

                }

            }
        }
    }

    @ColorInt
    public static int generateRandomColor() {
        Random randomColorTint = new Random();
        return Color.argb(255, randomColorTint.nextInt(42), randomColorTint.nextInt(256), randomColorTint.nextInt(214) + 42);
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return isExternalStorageWritable() ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static void hideKeyboard(View view, Activity activity){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
