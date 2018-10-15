package messenger.messenger.messanger.messenger.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.utils.JsonUtils;
import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.Constants;
import messenger.messenger.messanger.messenger.api.RetrofitClient;
import messenger.messenger.messanger.messenger.model.AppLaunchCountModel;
import messenger.messenger.messanger.messenger.model.CardScreenType;
import messenger.messenger.messanger.messenger.model.ExternalAppModel;
import messenger.messenger.messanger.messenger.utils.AppLaunchCountHelper;
import timber.log.Timber;

import static app.common.utils.Utils.readJsonFile;


/**
 * An implementation of AsyncTaskLoader which loads a {@code List<AppLaunchCountModel>}
 * containing all installed applications on the device.
 * Created by bedprakash.r on 14/01/18.
 */
@SuppressWarnings("WeakerAccess")
public class AppListLoader extends AsyncTaskLoader<List<TypeAwareModel>> {
    private static final String TAG = "ADP_AppListLoader";
    private static final boolean DEBUG = true;
    private static final Comparator<AppLaunchCountModel> LAUNCH_COUNT = (object1, object2) -> object2.getLaunchCount() - object1.getLaunchCount();
    public final PackageManager mPm;
    private List<TypeAwareModel> mApps;
    private HashMap<String, Integer> trackingPackages = new HashMap<>();

    public AppListLoader(Context ctx) {
        super(ctx);
        mPm = getContext().getPackageManager();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TypeAwareModel> loadInBackground() {
        Timber.d("%s+++ loadInBackground() called! +++", TAG);

        try {
            // load map from assets
            trackingPackages = JsonUtils.fromJsonToObj(readJsonFile(Constants.APPS_LIST_JSON),
                    trackingPackages.getClass());
        } catch (IOException ignored) {

        }

        if (trackingPackages == null) {
            trackingPackages = new HashMap<>();
        }


        // Retrieve all installed applications.
        List<ApplicationInfo> apps = mPm.getInstalledApplications(0);

        if (apps == null) {
            apps = new ArrayList<>();
        }


        AppLaunchCountHelper.resetTotalLaunchCount();

        // Create corresponding array of entries and load their labels.
        List<AppLaunchCountModel> entries = new ArrayList<>(apps.size());
        for (int i = 0; i < apps.size(); i++) {
            ApplicationInfo packageInfo = apps.get(i);

            if (packageInfo == null) {
                continue;
            }

            if (!trackingPackages.containsKey(packageInfo.packageName)) {
                continue;
            }

            AppLaunchCountModel entry = new AppLaunchCountModel();
            int appLaunchCount = AppLaunchCountHelper.getLaunchCount(packageInfo.packageName);
            entry.setLaunchCount(appLaunchCount);
            AppLaunchCountHelper.increaseTotalLaunchCount(appLaunchCount);
            CharSequence label = packageInfo.loadLabel(mPm);
            entry.setmLabel(label != null ? label.toString() : packageInfo.packageName);
            entry.setPackageName(packageInfo.packageName);
            entries.add(entry);
        }

        // Sort the list.
        Collections.sort(entries, LAUNCH_COUNT);

        ArrayList<ExternalAppModel> appCards = null;
        try {
            appCards = RetrofitClient.getMessengerClient().getAppCardForLandingScreen(CardScreenType.MAIN_SCREEN).execute().body();
        } catch (IOException e) {
            Timber.e(e);
        }


        List<TypeAwareModel> list = new ArrayList<>();
        list.addAll(entries);
        if (!AppUtils.isEmpty(appCards)) {
            Collections.sort(appCards, (model, anotherModel) -> anotherModel.viewOrder - model.viewOrder);
            list.addAll(appCards);
            return list;
        }

        return list;
    }

    @Override
    public void deliverResult(List<TypeAwareModel> apps) {
        if (isReset()) {
            Timber.w("%s+++ Warning! An async query came in while the Loader was reset! +++", TAG);
            // The Loader has been reset; ignore the result and invalidate the data.
            // This can happen when the Loader is reset while an asynchronous query
            // is working in the background. That is, when the background thread
            // finishes its work and attempts to deliver the results to the client,
            // it will see here that the Loader has been reset and discard any
            // resources associated with the new data as necessary.
            if (apps != null) {
                releaseResources(apps);
                return;
            }

        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<TypeAwareModel> oldApps = mApps;
        mApps = apps;

        if (isStarted()) {
            if (DEBUG) {
                Timber.d(TAG + "+++ Delivering results to the LoaderManager for" +
                        " the ListFragment to display! +++");
            }
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(apps);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldApps != null && oldApps != apps) {
            Timber.d("%s+++ Releasing any old data associated with this Loader. +++", TAG);
            releaseResources(oldApps);
        }
    }

    @Override
    protected void onStartLoading() {
        Timber.d("%s+++ onStartLoading() called! +++", TAG);

        if (mApps != null) {
            // Deliver any previously loaded data immediately.
            if (DEBUG) {
                Timber.d("%s+++ Delivering previously loaded data to the client...", TAG);
            }
            deliverResult(mApps);
        }

        // Register the observers that will notify the Loader when changes are made.
//        if (mAppsObserver == null) {
//            mAppsObserver = new InstalledAppsObserver(this);
//        }
//
//        if (mLocaleObserver == null) {
//            mLocaleObserver = new SystemLocaleObserver(this);
//        }

        if (takeContentChanged()) {
            // When the observer detects a new installed application, it will call
            // onContentChanged() on the Loader, which will cause the next call to
            // takeContentChanged() to return true. If this is ever the case (or if
            // the current data is null), we force a new load.
            Timber.d("%s+++ A content change has been detected... so force load! +++", TAG);
            forceLoad();
        } else if (mApps == null) {
            // If the current data is null... then we should make it non-null! :)
            if (DEBUG) {
                Timber.d("%s+++ The current data is data is null... so force load! +++", TAG);
            }
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        Timber.d("%s+++ onStopLoading() called! +++", TAG);

        // The Loader has been put in a stopped state, so we should attempt to
        // cancel the current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is; Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        if (DEBUG) {
            Timber.d("%s+++ onReset() called! +++", TAG);
        }

        onStopLoading();

        if (mApps != null) {
            releaseResources(mApps);
            mApps = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
//        if (mAppsObserver != null) {
//            getContext().unregisterReceiver(mAppsObserver);
//            mAppsObserver = null;
//        }
//
//        if (mLocaleObserver != null) {
//            getContext().unregisterReceiver(mLocaleObserver);
//            mLocaleObserver = null;
//        }
    }

    @Override
    public void onCanceled(List<TypeAwareModel> apps) {
        Timber.d("%s+++ onCanceled() called! +++", TAG);

        super.onCanceled(apps);
        releaseResources(apps);
    }

    @Override
    public void forceLoad() {
        Timber.d("%s+++ forceLoad() called! +++", TAG);
        super.forceLoad();
    }

//    // An observer to notify the Loader when new apps are installed/updated.
//    private InstalledAppsObserver mAppsObserver;
//
//    // The observer to notify the Loader when the system Locale has been changed.
//    private SystemLocaleObserver mLocaleObserver;

    private void releaseResources(List<TypeAwareModel> appList) {
        if (appList == null) {
            return;
        }
        Timber.d("%s+++ appList called! +++ %d", TAG, appList.size());

    }
}