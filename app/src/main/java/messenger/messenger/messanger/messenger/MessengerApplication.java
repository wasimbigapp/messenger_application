package messenger.messenger.messanger.messenger;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;


import com.squareup.picasso.Picasso;

import app.common.utils.ApplicationContextHolder;
import messenger.messenger.messanger.messenger.utils.AppIconRequestHandler;
import timber.log.Timber;

/**
 * Created by bedprakash.r on 07/01/18.
 */

public class MessengerApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private static MessengerApplication sInstance;
    private boolean appStatus = false;

    public static MessengerApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        ApplicationContextHolder.getInstance().setApplication(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        Picasso.setSingletonInstance(new Picasso.Builder(this)
                .addRequestHandler(new AppIconRequestHandler(this))
                .build());
        registerActivityLifecycleCallbacks(this);
    }

    public boolean isAppInBackground() {
        return !appStatus;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        appStatus = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        appStatus = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
