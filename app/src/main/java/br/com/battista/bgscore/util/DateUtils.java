package br.com.battista.bgscore.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {
    private static final String TAG = DateUtils.class.getSimpleName();

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static SimpleDateFormat formatWithHours = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    private DateUtils() {
    }

    public static String format(Calendar date) {
        Log.d(TAG, "format: Format the date for string with formatting: dd/MM/yyyy");
        if (date == null) {
            return "";
        }
        return format.format(date.getTime());
    }

    public static String formatWithHours(Calendar date) {
        Log.d(TAG, "format: Format the date for string with formatting: dd/MM/yyyy");
        if (date == null) {
            return "";
        }
        return formatWithHours.format(date.getTime());
    }
}
