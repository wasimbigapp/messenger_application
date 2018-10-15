package messenger.messenger.messanger.messenger.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Timer;
import java.util.TimerTask;

import app.common.models.CommonConstants;
import app.common.utils.PrefUtils;
import app.common.utils.Utils;
import app.common.views.activities.BaseActivity;
import messenger.messenger.messanger.messenger.BuildConfig;
import messenger.messenger.messanger.messenger.Constants;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.NotificationType;
import timber.log.Timber;

import static app.common.utils.Utils.putNonPersonalizedData;
import static app.common.utils.Utils.userConsentGiven;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splash extends BaseActivity {


    private static final long AD_LOAD_TIME_OUT = 5000;
    private static final String TAG = "Splash";

    private InterstitialAd mInterstitialAd;
    private boolean adCancelled;
    private Timer waitTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d(TAG + "Values: " + getIntent().getExtras());
        setContentView(R.layout.activity_splash);
        PrefUtils.putInt(this, Constants.APP_LAUNCH_COUNT, PrefUtils.getInt(this, Constants.APP_LAUNCH_COUNT) + 1);
        if (handleNotification(getIntent().getExtras())) {
            return;
        }
        loadInterstitialAd();
        waitTimer = new Timer();
        waitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                adCancelled = true;
                Splash.this.runOnUiThread(() -> launchHomeScreen());
            }
        }, AD_LOAD_TIME_OUT);
    }

    @Override
    public void onPause() {
        waitTimer.cancel();
        adCancelled = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mInterstitialAd.isLoaded()) {
            if (Utils.canShowAds()) {
                mInterstitialAd.show();
            }
        } else if (adCancelled) {
            launchHomeScreen();
        }
    }

    private void launchHomeScreen() {
        if (handleIntent()) {
            return;
        }
        navigateAhead();
    }

    private void navigateAhead() {
        Intent i = new Intent(Splash.this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void loadInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_id));

        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (!userConsentGiven()) {
            putNonPersonalizedData(adRequest, null);
        }

        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice(getString(R.string.TEST_DEVICE));
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                launchHomeScreen();
            }


            @Override
            public void onAdFailedToLoad(int i) {
                launchHomeScreen();
            }

            @Override
            public void onAdLoaded() {
                if (!Utils.canShowAds()) {
                    return;
                }
                if (!adCancelled) {
                    if (Utils.canShowAds()) {
                        mInterstitialAd.show();
                    } else {
                        onAdFailedToLoad(CommonConstants.AD_FAILURE_CODE);
                    }
                }
            }
        });
        mInterstitialAd.loadAd(adRequest.build());
    }


    private boolean handleNotification(@Nullable Bundle bundle) {
        if (bundle == null) {
            return false;
        }

        Timber.d(TAG + "Values: " + bundle);
        final String type = bundle.getString("type");

        if (type == null) {
            return false;
        }

        Intent intent = null;

        switch (type) {
            case NotificationType.WEB_OPEN: {
                final String url = bundle.getString("url");
                if (url == null) {
                    return false;
                }
                intent = BrowserActivity.getLaunchIntent(getApplicationContext(), url, true);
                break;
            }
            default:

        }
        if (intent == null) {
            return false;
        }
        startActivity(intent);
        finish();
        return true;
    }

    private boolean handleIntent() {
        final String forwardAction = getIntent().getStringExtra(Constants.FORWARD_ACTION);
        if (forwardAction == null) {
            return false;
        }

        Intent intent = new Intent();
        intent.setAction(forwardAction);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            navigateAhead();
        }

        finish();
        return true;
    }
}
