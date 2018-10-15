package bpr10.git.crhometabs.chrometabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;

import bpr10.git.crhometabs.R;

public class ChromeUtils {
    public static final String KEY_SHOW_AD = "showAd";
    private static final String KEY_URL = "url";

    public static void openInInetrnalBrowser(@NonNull Context context, @NonNull String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            Intent i = new Intent(context.getPackageName() + ".internalBrowser");
            i.putExtra(KEY_URL, url);
            i.putExtra(KEY_SHOW_AD, false);
            i.addCategory("android.intent.category.DEFAULT");
            context.startActivity(i);
        } catch (Exception ignored) {

        }
    }

    public static void openInExternalBrowser(@NonNull Context context, @NonNull String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }

            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(context.getResources().getColor(R.color.colorPrimary))
                    .build();

            CustomTabActivityHelper.openCustomTab(
                    context,// activity
                    customTabsIntent,
                    Uri.parse(url),
                    new WebviewFallback()
            );
        } catch (Exception ignored) {

        }
    }


    public static boolean isChromeTabSupported(Context context) {
        String chromePackageName = "com.android.chrome";
        int chromeTargetVersion = 45;

        boolean isSupportCustomTab = false;
        try {
            String chromeVersion = context.getPackageManager().getPackageInfo(chromePackageName, 0).versionName;
            if (chromeVersion.contains(".")) {
                chromeVersion = chromeVersion.substring(0, chromeVersion.indexOf('.'));
            }
            isSupportCustomTab = (Integer.valueOf(chromeVersion) >= chromeTargetVersion);
        } catch (Exception ignored) {
        }
        return isSupportCustomTab;
    }

}
