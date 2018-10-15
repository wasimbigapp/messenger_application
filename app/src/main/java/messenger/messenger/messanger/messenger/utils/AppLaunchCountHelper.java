package messenger.messenger.messanger.messenger.utils;

import android.content.SharedPreferences;

import app.common.utils.PrefUtils;
import messenger.messenger.messanger.messenger.MessengerApplication;
import messenger.messenger.messanger.messenger.model.AppLaunchCountUpdate;

/**
 * Class to save nad read app launch counts
 * Created by bedprakash.r on 09/01/18.
 */

public class AppLaunchCountHelper {

    private static final String LAUNCH_PREFERENCE_FILE_NAME = "launch_count";
    private static int totalLaunchCount = 0;

    public static int getLaunchCount(String packageName) {
        return PrefUtils.getInt(PrefUtils.getPreferences(MessengerApplication.getInstance(), LAUNCH_PREFERENCE_FILE_NAME), packageName);
    }

    public static void saveLaunchCount(String packageName, int count) {
        PrefUtils.putInt(PrefUtils.getPreferences(MessengerApplication.getInstance(), LAUNCH_PREFERENCE_FILE_NAME), packageName, count);
        increaseTotalLaunchCount(1);
        broadCastTotalLaunchCount();
    }

    public static void increaseTotalLaunchCount(int applaunchCount) {
        totalLaunchCount += applaunchCount;
    }

    public static void resetTotalLaunchCount() {
        totalLaunchCount = 0;
    }

    public static int getTotalLaunchCount() {
        return totalLaunchCount;
    }

    public static void broadCastTotalLaunchCount() {
        BusProvider.getUiBus().post(new AppLaunchCountUpdate(totalLaunchCount));
    }

    public static void clearAppLaunchCountData() {
        SharedPreferences sharedPref = PrefUtils.getPreferences(MessengerApplication.getInstance(), LAUNCH_PREFERENCE_FILE_NAME);
        sharedPref.edit().clear().commit();
    }

}
