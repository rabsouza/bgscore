package br.com.battista.bgscore.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {
    public static final int HOURS_IN_SECS = 3600;
    public static final int HOURS_IN_MINS = 60;
    private static final String TAG = DateUtils.class.getSimpleName();
    private static final String FORMART_HOURS_MINUTES = "%02d:%02d";
    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static SimpleDateFormat formatWithHours = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    private DateUtils() {
    }

    public static String format(Calendar date) {
        if (date == null) {
            return "";
        }
        return format.format(date.getTime());
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

        return String.format(FORMART_HOURS_MINUTES, hours, minutes);
    }
}
