package app.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import app.common.models.CommonConstants;
import bpr10.git.common.BuildConfig;
import bpr10.git.common.R;
import io.fabric.sdk.android.Fabric;

public class Utils {
    public static void launchUrl(Context context, String url) {

        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("market://")) {
            url = "http://" + url;
        }
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            context.startActivity(browserIntent);
        } catch (Exception e) {
            showErrorToast();
        }
    }

    @UiThread
    public static void showShortToast(String message) {
        try {
//            if (!MessengerApplication.getInstance().isAppInBackground()) {
//                Logger.debug("Toast", message);
            Toast.makeText(ApplicationContextHolder.getInstance().getContext(), message, Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            Utils.logErrorMessage("showShortToast " + e.getMessage());
        }
    }

    @UiThread
    public static void showShortToast(@StringRes int stringId) {
        showShortToast(Utils.getString(stringId));
    }

    public static String getString(@StringRes int stringRes) {
        return ApplicationContextHolder.getInstance().getContext().getString(stringRes);
    }

    public static int getDimens(@DimenRes int dimenRes) {
        return (int) ApplicationContextHolder.getInstance().getContext().getResources().getDimension(dimenRes);
    }

    public static void putNonPersonalizedData(AdRequest.Builder adRequest, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("npa", "1");
        adRequest.addNetworkExtrasBundle(AdMobAdapter.class, bundle);
    }

    public static boolean isConsentGiven() {
        return PrefUtils.getBoolean(ApplicationContextHolder.getInstance().getApplication(), CommonConstants.IS_CONSENT_GIVEN, false);
    }

    public static boolean userConsentGiven() {
        Context instance = ApplicationContextHolder.getInstance().getApplication();
        if (!isLocationEU(instance)) {
            return true;
        }

        return isConsentGiven();
    }

    private static boolean isLocationEU(Context context) {
        return ConsentInformation.getInstance(context).isRequestLocationInEeaOrUnknown();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String readJsonFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(getAssetsPath().open(filename))));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        return sb.toString();
    }

    @NonNull
    public static AssetManager getAssetsPath() {
        return ApplicationContextHolder.getInstance().getContext().getAssets();
    }


    @UiThread
    public static void showErrorToast() {
        showShortToast(R.string.error_generic);
    }


    public static void logException(Throwable e) {
        if (Fabric.isInitialized()) {
            Crashlytics.logException(e);
        }
    }

    public static void logErrorMessage(String message) {
        if (Fabric.isInitialized()) {
            Crashlytics.log(message);
        }
    }


    public static String getAndroidId() {
        return ApplicationContextHolder.getInstance().getAndroidId();
    }

    public static String getReadableTime(int timeStamp) {
        return getReadableTime(System.currentTimeMillis() / 1000, new Date(timeStamp).getTime());
    }

    public static String getReadableTime(long newerTimeStamp, long olderTimeStamp) {
        return TimeHelper.Companion.getTime(newerTimeStamp, olderTimeStamp);
    }

    public static boolean canShowAds() {
        return PrefUtils.getBoolean(ApplicationContextHolder.getInstance().getApplication(), CommonConstants.CAN_SHOW_ADS, true);
    }

    public static boolean isEmpty(List items) {
        return items == null || items.isEmpty();
    }

    public static String getNativeAdId() {

        if (BuildConfig.DEBUG) {
            return "ca-app-pub-3940256099942544/2247696110";
        } else {
            return Utils.getString(R.string.native_ad_id);
        }
    }

    public static AdRequest getAdRequestData() {
        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (!Utils.userConsentGiven()) {
            Utils.putNonPersonalizedData(adRequest, null);
        }
        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice(Utils.getString(R.string.TEST_DEVICE));
        }
        return adRequest.build();
    }
}
