package app.common.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import bpr10.git.common.BuildConfig;

/**
 * Created by bedprakash.r on 16/03/18.
 */

public class Logger {
    public static boolean logsEnabled = BuildConfig.LOGS_ENABLED;

    private Logger() {
    }

    public static int verbose(String message) {
        return verbose("MessengerApp", message);
    }

    public static int verbose(String tag, String message) {
        return !logsEnabled ? 0 : Log.v(tag, message);
    }

    public static int debug(String message) {
        return debug("MessengerApp", message);
    }

    public static int debug(String tag, String message) {
        return !logsEnabled ? 0 : Log.d(tag, message);
    }

    public static int info(String message) {
        return info("MessengerApp", message);
    }

    public static int info(String tag, String message) {
        return !logsEnabled ? 0 : Log.i(tag, message);
    }

    public static int warn(String message) {
        return warn("MessengerApp", message);
    }

    public static int warn(String tag, String message) {
        return !logsEnabled ? 0 : Log.w(tag, message);
    }

    public static int warn(String message, Throwable tr) {
        return warn("MessengerApp", message, tr);
    }

    public static int warn(String tag, String message, Throwable tr) {
        return !logsEnabled ? 0 : Log.w(tag, message, tr);
    }

    public static int error(String tag, String message, Throwable tr) {
        return !logsEnabled ? 0 : Log.e(tag, message, tr);
    }

    public static int error(String tag, String msg) {
        return !logsEnabled ? 0 : Log.e(tag, msg);
    }

    @SuppressLint("LogNotTimber")
    public static void printStackTrace(Throwable throwable) {
        Log.e("", "", throwable);
    }
}
