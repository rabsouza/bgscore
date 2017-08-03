package br.com.battista.bgscore.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final int HOURS_IN_SECS = 3600;
    public static final int HOURS_IN_MINS = 60;
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";

    private static final String TAG = DateUtils.class.getSimpleName();
    private static final String FORMAT_HOURS_MINUTES = "%02d:%02d";
    private static SimpleDateFormat format = new SimpleDateFormat(DD_MM_YYYY, Locale.getDefault());
    private static SimpleDateFormat formatWithHours =
            new SimpleDateFormat(DD_MM_YYYY_HH_MM, Locale.getDefault());

    private DateUtils() {
    }

    public static String format(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        return format(now);
    }

    public static String format(Calendar date) {
        if (date == null) {
            return "";
        }
        return format.format(date.getTime());
    }

    public static Date parse(String date) {
        if (date == null) {
            return null;
        }
        try {
            return format.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "parse: Error parse to DAte", e);
            return new Date();
        }
    }

    public static String formatWithHours(Calendar date) {
        if (date == null) {
            return "";
        }
        return formatWithHours.format(date.getTime());
    }

    public static String convertSecToHours(Long timeInSec) {
        if (timeInSec == null) {
            return "";
        }
        long hours = timeInSec / HOURS_IN_SECS;
        long minutes = (timeInSec % HOURS_IN_SECS) / HOURS_IN_MINS;

        return String.format(FORMAT_HOURS_MINUTES, hours, minutes);
    }
}
