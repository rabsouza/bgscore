package br.com.battista.bgscore.util;

import com.google.common.base.Strings;

import java.text.DateFormat;
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
    public static final String HH_MM = "HH:mm";
    public static final String REGEX_TIME = "(?m)^(\\d\\d:\\d\\d)";
    public static final String REGEX_DATE = "(?m)^(\\d\\d/\\d\\d/\\d\\d\\d\\d)";
    public static final String REGEX_DATE_WITH_HOURS = "(?m)^(\\d\\d/\\d\\d/\\d\\d\\d\\d\\s\\d\\d:\\d\\d)";

    private DateUtils() {
    }

    public static String formatHours(Date date) {
        if (date == null) {
            return "00:00";
        }
        return format(date, HH_MM);
    }

    public static String formatHours(Calendar date) {
        if (date == null) {
            return "00:00";
        }
        return formatHours(date.getTime());
    }

    public static String format(Date date) {
        if (date == null) {
            return "";
        }
        return format(date, DD_MM_YYYY);
    }

    public static String format(Calendar date) {
        if (date == null) {
            return "";
        }
        return format(date.getTime());
    }

    public static String formatWithHours(Calendar date) {
        if (date == null) {
            return "";
        }
        return format(date.getTime(), DD_MM_YYYY_HH_MM);
    }

    public static String formatWithHours(Date date) {
        if (date == null) {
            return "";
        }
        return format(date, DD_MM_YYYY_HH_MM);
    }

    private static String format(Date date, String parten) {
        DateFormat format = new SimpleDateFormat(parten, Locale.getDefault());
        return format.format(date);
    }

    public static Date parse(String date) {
        if (date == null) {
            return null;
        }

        Pattern patternDate = Pattern.compile(REGEX_DATE);
        if (!patternDate.matcher(date).find()) {
            return null;
        }

        return parse(date, DD_MM_YYYY);
    }

    public static Date parseWithHours(String date) {
        if (Strings.isNullOrEmpty(date)) {
            return null;
        }

        Pattern patternDate = Pattern.compile(REGEX_DATE_WITH_HOURS);
        if (!patternDate.matcher(date).find()) {
            return null;
        }

        return parse(date, DD_MM_YYYY_HH_MM);
    }

    public static Long parseTime(String time) {
        if (Strings.isNullOrEmpty(time)) {
            return 0L;
        }

        Pattern patternTime = Pattern.compile(REGEX_TIME);
        if (!patternTime.matcher(time).find()) {
            return 0L;
        }

        StringTokenizer tokenizer = new StringTokenizer(time, ":", false);
        Long hours = Long.valueOf(tokenizer.nextElement().toString());
        Long minutes = Long.valueOf(tokenizer.nextElement().toString());

        return hours * 60 + minutes;
    }

    private static Date parse(String date, String parten) {
        DateFormat format = new SimpleDateFormat(parten, Locale.getDefault());
        try {
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
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

}
