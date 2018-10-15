package app.common.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import app.common.models.CommonConstants;
import bpr10.git.common.BuildConfig;
import bpr10.git.common.R;
import io.fabric.sdk.android.Fabric;

import static app.common.utils.Utils.getString;

public class ApplicationContextHolder {

    private static ApplicationContextHolder sInstance;
    private Application application;
    private String androidId;
    private ViewModelProvider.Factory factory;
    private ViewModel newsModel;
    private String adID;

    private ApplicationContextHolder() {

    }

    public static ApplicationContextHolder getInstance() {
        if (sInstance == null) {
            synchronized (ApplicationContextHolder.class) {
                if (sInstance == null) {
                    sInstance = new ApplicationContextHolder();
                }
            }
        }

        return sInstance;
    }

    public Application getContext() {
        return application;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(final Application application) {
        this.application = application;

        androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        factory = new ViewModelProvider.AndroidViewModelFactory(this.application);

        String[] publisherIds = new String[]{getString(R.string.publisher_id)};
        ConsentInformation.getInstance(application).addTestDevice(getString(R.string.gdpr_test_device));
        ConsentInformation.getInstance(application).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_DISABLED);

        ConsentInformation.getInstance(application).requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                PrefUtils.putBoolean(application, CommonConstants.IS_REQUEST_LOCATION_IN_EEA_OR_UNKNOWN, ConsentInformation.getInstance(application).isRequestLocationInEeaOrUnknown());
                if (ConsentInformation.getInstance(application).isRequestLocationInEeaOrUnknown()) {
                    controlTrackingServices(application, false);
                } else {
                    controlTrackingServices(application, true);
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String s) {
                Log.e(application.getClass().getSimpleName(), "onFailedToUpdateConsentInfo: " + s);
            }
        });

    }

    public String getAndroidId() {
        return androidId;
    }

    public ViewModelProvider.Factory getFactory() {
        return factory;
    }

    public void setFactory(ViewModelProvider.Factory factory) {
        this.factory = factory;
    }

    public ViewModel getNewsModel() {
        return newsModel;
    }

    public void setNewsModel(ViewModel newsModel) {
        this.newsModel = newsModel;
    }

    @SuppressLint("MissingPermission")
    public void controlTrackingServices(Context context, boolean enable) {

        enable = enable || Utils.userConsentGiven();

        FirebaseMessaging.getInstance().setAutoInitEnabled(enable);
        FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(enable);
        if (enable && !BuildConfig.DEBUG) {
            Fabric.with(context, new Crashlytics());
        }
    }

    public void saveAdID(String adId) {
        this.adID = adId;
    }

    public String getAdID() {
        return adID;
    }
}
