package app.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Preference Helper
 * Created by bedprakash.r on 07/01/18.
 */

public class PrefUtils {

    public static final String PREFERENCES = "app_preferences";

    // Getters
    private static SharedPreferences getPreferences(Context ctx) {
        return getPreferences(ctx, PREFERENCES);
    }

    // Getters
    public static SharedPreferences getPreferences(Context ctx, String prefFileName) {
        return ctx.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    public static String getString(Context ctx, String key, String defvalue) {
        SharedPreferences prefs = getPreferences(ctx);
        return prefs.getString(key, defvalue);
    }

    public static String getString(Context ctx, String key) {
        return getString(ctx, key, "");
    }

    public static int getInt(Context ctx, String key) {
        SharedPreferences prefs = getPreferences(ctx);
        return getInt(prefs, key);
    }

    public static int getInt(SharedPreferences prefs, String key) {
        return prefs.getInt(key, 0);
    }

    public static long getLong(Context ctx, String key) {
        SharedPreferences prefs = getPreferences(ctx);
        return prefs.getLong(key, 0L);
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        SharedPreferences prefs = getPreferences(ctx);
        return prefs.getBoolean(key, defValue);
    }

    public static float getFloat(Context ctx, String key) {
        return getFloat(ctx, key, 0f);
    }

    public static float getFloat(Context ctx, String key, float defValue) {
        SharedPreferences prefs = getPreferences(ctx);
        return prefs.getFloat(key, defValue);
    }

    // Setters

    public static void putString(Context ctx, String key, String value) {
        SharedPreferences.Editor editor = getPreferences(ctx).edit();
        editor.putString(key, value).apply();
    }

    public static void putBoolean(Context ctx, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences(ctx).edit();
        editor.putBoolean(key, value).apply();
    }

    public static void putLong(Context ctx, String key, long value) {
        SharedPreferences.Editor editor = getPreferences(ctx).edit();
        editor.putLong(key, value).apply();
    }

    public static void putInt(Context ctx, String key, int value) {
        putInt(getPreferences(ctx), key, value);
    }

    public static void putInt(@NonNull SharedPreferences pref, String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value).apply();
    }


    public static void putFloat(Context ctx, String key, float value) {
        SharedPreferences.Editor editor = getPreferences(ctx).edit();
        editor.putFloat(key, value).apply();
    }

    public static void remove(Context ctx, String key) {
        SharedPreferences.Editor editor = getPreferences(ctx).edit();
        editor.remove(key).apply();
    }

    public static void clear(Context ctx) {
        SharedPreferences.Editor editor = getPreferences(ctx).edit();
        editor.clear().apply();
    }
}
