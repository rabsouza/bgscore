package br.com.battista.bgscore.util;

import android.util.Log;

import br.com.battista.bgscore.BuildConfig;

public class LogUtils {

    private LogUtils() {
    }

    public static void v(String tag, String msg) {
        if (isLoggable())
            Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isLoggable())
            Log.v(tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        if (isLoggable())
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isLoggable())
            Log.d(tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        if (isLoggable())
            Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isLoggable())
            Log.i(tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        if (isLoggable())
            Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isLoggable())
            Log.w(tag, msg, tr);
    }

    public static boolean isLoggable() {
        return BuildConfig.LOGGLABE;
    }

    public static void w(String tag, Throwable tr) {
        if (isLoggable())
            Log.w(tag, tr);
    }

    public static void e(String tag, String msg) {
        if (isLoggable())
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isLoggable())
            Log.e(tag, msg, tr);
    }

    public static void wtf(String tag, String msg) {
        if (isLoggable())
            Log.wtf(tag, msg);
    }

    public static void wtf(String tag, Throwable tr) {
        if (isLoggable())
            Log.wtf(tag, tr);
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        if (isLoggable())
            Log.wtf(tag, msg, tr);
    }

}
