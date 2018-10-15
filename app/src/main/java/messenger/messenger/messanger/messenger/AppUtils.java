package messenger.messenger.messanger.messenger;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import app.common.utils.JsonUtils;
import app.common.utils.PrefUtils;
import app.common.utils.Utils;
import io.fabric.sdk.android.Fabric;
import messenger.messenger.messanger.messenger.model.CurtainConfig;
import messenger.messenger.messanger.messenger.views.RateDialog;

import static app.common.utils.Utils.getString;
import static app.common.utils.Utils.showErrorToast;

;

/**
 * Class to hold utility methods
 * Created by bedprakash.r on 12/01/18.
 */
public class AppUtils {

    public static final String ASSET_BASE_PATH = "../app/src/main/assets/";

    public static void launchPackage(String packageName) {
        Context context = MessengerApplication.getInstance();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent == null) {
            showErrorToast();
            return;
        }
        try {
            context.startActivity(launchIntent);
        } catch (Exception e) {
            showErrorToast();
        }
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public static void runOnAsyncTask(final Runnable runnable) {
        new RunnableAsyncTask(runnable).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void openReviewMailer(@NonNull Context context) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:contact@allin1messenger.com?subject=Messenger%20app%20review"));
            context.startActivity(emailIntent);
        } catch (Exception e) {
            showErrorToast();
        }
    }

    public static void openAppPlayStore(@NonNull Context context, @NonNull String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            } catch (Exception e) {
                showErrorToast();
            }
        }
    }

    public static void launchRateDialog(FragmentManager manager) {
        new RateDialog().show(manager, null);
    }

    public static String getPackageName() {
        return MessengerApplication.getInstance().getPackageName();
    }

    public static void shareApp(Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.share_text, getString(R.string.share_url)));
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.share_text_subject));
        try {
            context.startActivity(sharingIntent);
        } catch (Exception e) {
            showErrorToast();
        }
    }

    @NonNull
    public static Bitmap drawableToBitmap(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }


    public static int getSmallBannerPosition(@Nullable List items) {
        if (items == null) {
            return 0;
        }
        return PrefUtils.getInt(MessengerApplication.getInstance(), Constants.SMALL_AD_POSITION) % items.size();
    }

    public static int getBigBannerPosition(@Nullable List items) {
        if (Utils.isEmpty(items)) {
            return 0;
        }
        return PrefUtils.getInt(MessengerApplication.getInstance(), Constants.BIG_AD_POSITION) % items.size();
    }

    public static void incrementSmallBannerPosition() {
        PrefUtils.putInt(MessengerApplication.getInstance(), Constants.SMALL_AD_POSITION, (PrefUtils.getInt(MessengerApplication.getInstance(), Constants.SMALL_AD_POSITION) + 1));
    }

    public static void incrementBigBannerPosition() {
        PrefUtils.putInt(MessengerApplication.getInstance(), Constants.BIG_AD_POSITION, (PrefUtils.getInt(MessengerApplication.getInstance(), Constants.BIG_AD_POSITION) + 1));
    }

    public static class RunnableAsyncTask extends AsyncTask {

        private final Runnable runnable;

        RunnableAsyncTask(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            runnable.run();
            return null;
        }
    }

    public static int getScreenWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void saveCurtainConfig(@NonNull CurtainConfig config) {
        PrefUtils.putString(MessengerApplication.getInstance(), Constants.CURTAIN_CONFIG, JsonUtils.toJsonObj(config));
    }

    @NonNull
    public static CurtainConfig getCurtainConfig() {
        String curtainConfigString = PrefUtils.getString(MessengerApplication.getInstance(), Constants.CURTAIN_CONFIG);
        CurtainConfig curtainConfig;
        if (curtainConfigString == null) {
            curtainConfig = new CurtainConfig();
        } else {
            curtainConfig = JsonUtils.fromJsonToObj(curtainConfigString, CurtainConfig.class);
        }

        if (curtainConfig == null) {
            curtainConfig = new CurtainConfig();
        }
        return curtainConfig;
    }

    public static int dpToPx(@NonNull Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        return (px < 1.0f) ? 0 : (int) px;
    }

    @SuppressLint("NewApi")
    public static boolean canDrawOverlayViews(Context con) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        try {
            return Settings.canDrawOverlays(con);
        } catch (NoSuchMethodError e) {
            return canDrawOverlaysUsingReflection(con);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean canDrawOverlaysUsingReflection(Context context) {

        try {

            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class clazz = AppOpsManager.class;
            Method dispatchMethod = clazz.getMethod("checkOp", new Class[]{int.class, int.class, String.class});
            //AppOpsManager.OP_SYSTEM_ALERT_WINDOW = 24
            int mode = (Integer) dispatchMethod.invoke(manager, new Object[]{24, Binder.getCallingUid(), context.getApplicationContext().getPackageName()});

            return AppOpsManager.MODE_ALLOWED == mode;

        } catch (Exception e) {
            return false;
        }

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

}
