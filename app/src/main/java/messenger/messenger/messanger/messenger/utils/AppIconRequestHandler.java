package messenger.messenger.messanger.messenger.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.Constants;

import static com.squareup.picasso.Picasso.LoadedFrom.DISK;

/**
 * AppIconRequestHandler
 * Created by bedprakash.r on 16/01/18.
 */

public class AppIconRequestHandler extends RequestHandler {
    public static final String SCHEME_PNAME = "pname";

    private final PackageManager pm;
    private final int dpi;
    private Bitmap defaultAppIcon;

    public AppIconRequestHandler(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        dpi = am.getLauncherLargeIconDensity();
        pm = context.getPackageManager();
    }

    public static Uri getAppUri(String packageName) {
        return Uri.parse(AppIconRequestHandler.SCHEME_PNAME + Constants.SCHEME_SEPARATOR + packageName);
    }

    @Override
    public boolean canHandleRequest(Request data) {
        return data.uri != null && TextUtils.equals(data.uri.getScheme(), SCHEME_PNAME);
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        try {
            return new Result(getFullResIcon(request.uri.toString().split(Constants.SCHEME_SEPARATOR)[1]), DISK);
        } catch (Exception e) {
            return null;
        }
    }

    private Bitmap getFullResIcon(String packageName) throws PackageManager.NameNotFoundException {
        return getFullResIcon(pm.getApplicationInfo(packageName, 0));
    }

    private Bitmap getFullResIcon(ApplicationInfo info) {
        try {
            Resources resources = pm.getResourcesForApplication(info.packageName);
            if (resources != null) {
                int iconId = info.icon;
                if (iconId != 0) {
                    return getFullResIcon(resources, iconId);
                }
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return getFullResDefaultActivityIcon();
    }

    private Bitmap getFullResIcon(Resources resources, int iconId) {
        final Drawable drawable;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                drawable = resources.getDrawableForDensity(iconId, dpi, null);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                drawable = resources.getDrawableForDensity(iconId, dpi);
            } else {
                drawable = resources.getDrawable(iconId);
            }
        } catch (Resources.NotFoundException e) {
            return getFullResDefaultActivityIcon();
        }
        return AppUtils.drawableToBitmap(drawable);
    }

    private Bitmap getFullResDefaultActivityIcon() {
        if (defaultAppIcon == null) {
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                drawable = Resources.getSystem().getDrawableForDensity(
                        android.R.mipmap.sym_def_app_icon, dpi);
            } else {
                drawable = Resources.getSystem().getDrawable(
                        android.R.drawable.sym_def_app_icon);
            }
            defaultAppIcon = AppUtils.drawableToBitmap(drawable);
        }
        return defaultAppIcon;
    }
}
