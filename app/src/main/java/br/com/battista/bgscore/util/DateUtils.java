package br.com.battista.bgscore.util;

import com.google.common.base.Strings;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class DateUtils {

    public static final int MIN_IN_MILLIS = 60000;
    public static final int HOURS_IN_MINS = 60;
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
    public static final String REGEX_TIME = "(?m)^(\\d\\d:\\d\\d)";
    private static final String TAG = DateUtils.class.getSimpleName();
    private static SimpleDateFormat format = new SimpleDateFormat(DD_MM_YYYY, Locale.getDefault());
    private static SimpleDateFormat formatWithHours = new SimpleDateFormat(DD_MM_YYYY_HH_MM, Locale.getDefault());
    private static Pattern patternTime = Pattern.compile(REGEX_TIME);

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
            LogUtils.e(TAG, "parse: Error parse to DAte", e);
            return new Date();
        }
    }

    public static Date parseWithHours(String date) {
        if (date == null) {
            return null;
        }
        try {
            return formatWithHours.parse(date);
        } catch (ParseException e) {
            LogUtils.e(TAG, "parse: Error parse to DAte", e);
            return new Date();
        }
    }

    public static Long parseTime(String time) {
        if (Strings.isNullOrEmpty(time)) {
            return 0L;
        }

        if (!patternTime.matcher(time).find()) {
            return 0L;
        }

        StringTokenizer tokenizer = new StringTokenizer(time, ":", false);
        Long hours = Long.valueOf(tokenizer.nextElement().toString());
        Long minutes = Long.valueOf(tokenizer.nextElement().toString());

        return hours * 60 + minutes;
    }

    public static String formatTime(Long time) {
        if (time == null || time == 0) {
            return "00:00";
        }

        Long minute = time % HOURS_IN_MINS;
        Long hour = time / HOURS_IN_MINS;

        DecimalFormat decimalFormatScore = new DecimalFormat("#00");
        StringBuilder newTime = new StringBuilder();
        newTime.append(decimalFormatScore.format(hour))
                .append(":")
                .append(decimalFormatScore.format(minute));
        return newTime.toString();
    }

    public static String formatWithHours(Calendar date) {
        if (date == null) {
            return "";
        }
        return formatWithHours.format(date.getTime());
    }

    public static String formatWithHours(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        return formatWithHours(now);
    }


}
