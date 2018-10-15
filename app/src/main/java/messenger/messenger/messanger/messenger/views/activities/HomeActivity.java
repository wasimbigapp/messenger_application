package messenger.messenger.messanger.messenger.views.activities;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.firebase.FirebaseApp;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import app.common.adapters.FragmentPayload;
import app.common.adapters.PageTypes;
import app.common.models.CommonConstants;
import app.common.utils.ApplicationContextHolder;
import app.common.utils.Logger;
import app.common.utils.PrefUtils;
import app.common.utils.Utils;
import app.common.views.activities.BaseActivity;
import bpr10.git.crhometabs.chrometabs.ChromeUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import messenger.messenger.messanger.messenger.AppUtils;
import messenger.messenger.messanger.messenger.BuildConfig;
import messenger.messenger.messanger.messenger.Constants;
import messenger.messenger.messanger.messenger.R;
import messenger.messenger.messanger.messenger.model.AppLaunchCountUpdate;
import messenger.messenger.messanger.messenger.model.CurtainConfig;
import messenger.messenger.messanger.messenger.model.ExternalBrowserOpened;
import messenger.messenger.messanger.messenger.model.ReloadDataEvent;
import messenger.messenger.messanger.messenger.services.CurtainService;
import messenger.messenger.messanger.messenger.utils.AppLaunchCountHelper;
import messenger.messenger.messanger.messenger.utils.BusProvider;
import messenger.messenger.messanger.messenger.utils.CurtainToggleCallback;
import messenger.messenger.messanger.messenger.views.ConsentFragment;
import messenger.messenger.messanger.messenger.views.HomeAdapter;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static app.common.utils.Utils.putNonPersonalizedData;
import static app.common.utils.Utils.showShortToast;
import static app.common.utils.Utils.userConsentGiven;
import static messenger.messenger.messanger.messenger.AppUtils.canDrawOverlayViews;
import static messenger.messenger.messanger.messenger.AppUtils.drawableToBitmap;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ColorPickerDialogListener, CurtainToggleCallback {
    private static final String TAG = "HomeActivity";
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 101;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private InterstitialAd mInterstitialAd;
    private boolean ratingShown;
    private boolean adToBeOpened;
    private View imgNotification;
    private boolean showCurtain = false;
    private CurtainConfig curtainConfig;
    private boolean dialogShowing;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MobileAds.initialize(this, getString(R.string.AD_ID));
        initViews();

        FirebaseApp.initializeApp(this);
        curtainConfig = AppUtils.getCurtainConfig();

        showBannerAd();
        loadInterstitialAd();
        BusProvider.getUiBus().register(this);
        if (getIntent().getBooleanExtra(BrowserActivity.KEY_SHOW_AD, false)) {
            showInterstitialAd();
        }
        if (!userConsentGiven()) {
            showConsentForm();
        }
        getGoogleAdId();
    }

    private boolean checkForRateDialog() {
        if (PrefUtils.getBoolean(this, Constants.APP_RATE_DONE, false) || PrefUtils.getInt(this, Constants.APP_LAUNCH_COUNT) % 3 != 0) {
            return false;
        }
        AppUtils.launchRateDialog(getSupportFragmentManager());
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (adToBeOpened) {
            showInterstitialAd();
        }

        AppUtils.incrementBigBannerPosition();
        AppUtils.incrementSmallBannerPosition();

        handleAction(getIntent());
        checkForCurtainVisibility();

    }

    private void checkForCurtainVisibility() {
        if (showCurtain) {
            showCurtain = false;
            CurtainService.start(this);
        }
    }

    @Subscribe
    public void onTotalCountUpdate(AppLaunchCountUpdate update) {
        invalidateOptionsMenu();
    }

    private void loadInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(getInterstitialAdRequest());
            }
        });
        mInterstitialAd.loadAd(getInterstitialAdRequest());
    }

    private void showBannerAd() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest.Builder adRequest = new AdRequest.Builder();

        if (!userConsentGiven()) {
            putNonPersonalizedData(adRequest, null);
        }

        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice(getString(R.string.TEST_DEVICE));
        }

        if (Utils.canShowAds()) {
            mAdView.loadAd(adRequest.build());
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        View headerView = navigationView.getHeaderView(0);
        imgNotification = headerView.findViewById(R.id.img_notification);
        imgNotification.setOnClickListener(this);
        imgNotification.setSelected(PrefUtils.getBoolean(this, Constants.IS_NOTIFICATION_ENABLED, true));
        updateNotification();


        // adding pager and adapter
        ViewPager pager = findViewById(R.id.home_pager);
        List<FragmentPayload> items = new ArrayList<>();
        items.add(new FragmentPayload(PageTypes.FEATURED, PageTypes.FEATURED, null));
//        items.add(new FragmentPayload(PageTypes.FAST_BROWSING, PageTypes.FAST_BROWSING, null));
        items.add(new FragmentPayload(PageTypes.NEWS, PageTypes.NEWS, null));


        HomeAdapter adapter = new HomeAdapter(getSupportFragmentManager());
        adapter.updateItems(items);
        pager.setAdapter(adapter);

        //setting tabs
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (!ratingShown) {
            ratingShown = true;
            boolean showed = checkForRateDialog();
            if (showed) {
                return;
            }
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.app_exit_msg), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private void showInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            if (Utils.canShowAds()) {
                mInterstitialAd.show();
            }
            adToBeOpened = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        TextView txtTotalCountMenu = (TextView) menu.findItem(R.id.action_count).getActionView();
        txtTotalCountMenu.setText(String.format(getString(R.string.total_launch_count_menu), AppLaunchCountHelper.getTotalLaunchCount()));
        txtTotalCountMenu.setOnClickListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            AppUtils.shareApp(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_facebook) {
            // open url
            ChromeUtils.openInExternalBrowser(this, Constants.FACEBOOK_URL);
        } else if (id == R.id.nav_rate_us) {
            // show rating
            AppUtils.launchRateDialog(getSupportFragmentManager());
        } else if (id == R.id.nav_share) {
            // share text
            AppUtils.shareApp(this);
        } else if (id == R.id.nav_chat_curtain) {
            // share text
            showCurtainWithPermission();
        } else if (id == R.id.nav_chat_curtain_settings) {
            // chat curtain settings
            showColorPickerDialog();
        } else if (id == R.id.nav_clear) {
            // chat curtain toggle
            AppLaunchCountHelper.clearAppLaunchCountData();
            BusProvider.getUiBus().post(new ReloadDataEvent());
            recreate();
        } else if (id == R.id.nav_privacy_policy) {
            // chat curtain toggle
            ChromeUtils.openInExternalBrowser(this, getString(R.string.privacy_policy_url));
        } else if (id == R.id.nav_consent_form) {
            // consent form
            showConsentForm();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void showColorPickerDialog() {
        if (dialogShowing) {
            return;
        }
        dialogShowing = true;
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                .setAllowPresets(true)
                .setDialogId(R.id.settings_color_curtain)
                .setColor(Color.parseColor(curtainConfig.curtainColor))
                .show(this);
    }

    private boolean showCurtainWithPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !canDrawOverlayViews(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            return false;
        } else {
            showCurtain();
            return true;
        }
    }

    private void showCurtain() {
        if (mInterstitialAd.isLoaded()) {
            showCurtain = true;
            showInterstitialAd();
        } else {
            CurtainService.start(this);
        }
    }

    @Subscribe
    public void onExternalBrowserLaunched(ExternalBrowserOpened event) {
    }

    private void updateNotification() {
        if (imgNotification.isSelected()) {
            showStickyNotification();
        } else {
            hideStickyNotification();
        }
    }


    private void showStickyNotification() {
        Intent i = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Constants.REQ_CODE_NOTIFICATION, i, FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, Constants.MESSENGER_NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification_small)
                        .setLargeIcon(drawableToBitmap(getResources().getDrawable(R.drawable.ic_notification_big)))
                        .setContentTitle(getString(R.string.title_notification))
                        .setContentText(getString(R.string.desc_notification))
                        .setOngoing(true)
                        .setSound(null)
                        .setChannelId(Constants.MESSENGER_NOTIFICATION_CHANNEL_ID)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(Constants.MESSENGER_NOTIFICATION_CHANNEL_ID, Constants.MESSENGER_NOTIFICATION_CHANNEL_Name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setSound(null, null);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
            mBuilder.setChannelId(notificationChannel.getId());
        }
        if (notificationManager != null) {
            notificationManager.notify(Constants.STICKY_NOTIFICATION_ID, mBuilder.build());
        }
    }


    void hideStickyNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(Constants.STICKY_NOTIFICATION_ID);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_notification) {
            boolean isSelected = imgNotification.isSelected();
            PrefUtils.putBoolean(this, Constants.IS_NOTIFICATION_ENABLED, !isSelected);
            imgNotification.setSelected(!isSelected);
            updateNotification();
        } else if (i == R.id.txt_app_launch_count) {
            showShortToast(R.string.total_launch_count_menu_toast);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(BrowserActivity.KEY_SHOW_AD, false)) {
            adToBeOpened = true;
            showInterstitialAd();
        }
        handleAction(intent);
    }

    @Override
    protected void onDestroy() {
        BusProvider.getUiBus().unregister(this);
        imgNotification.setSelected(false);
        updateNotification();
        super.onDestroy();
    }

    public AdRequest getInterstitialAdRequest() {

        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (!userConsentGiven()) {
            putNonPersonalizedData(adRequest, null);
        }
        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice(getString(R.string.TEST_DEVICE));
        }
        return adRequest.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                showCurtain();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onColorSelected(int dialogId, int color) {
        dialogShowing = false;
        if (dialogId == R.id.settings_color_curtain) {
            curtainConfig.curtainColor = String.format("#%06X", 0xFFFFFF & color);
        }
        AppUtils.saveCurtainConfig(curtainConfig);
        BusProvider.getUiBus().post(curtainConfig);
    }

    @Override
    public void onColorChanged(int dialogId, int color) {
        curtainConfig.curtainColor = String.format("#%06X", 0xFFFFFF & color);
        AppUtils.saveCurtainConfig(curtainConfig);
        BusProvider.getUiBus().post(curtainConfig);
    }

    @Override
    public void onDialogDismissed(int dialogId) {
        dialogShowing = false;
    }

    private void handleAction(@NonNull Intent intent) {
        String action = intent.getAction();

        if (action == null) {
            return;
        }

        if (action.equals(Constants.ACTION_COLOR_PICKER)) {
            showColorPickerDialog();
        }
    }

    @Override
    public void handleCurtainToggle(CompoundButton compoundButton, boolean turnedOn) {
        if (turnedOn) {
            if (!showCurtainWithPermission()) {
                compoundButton.setChecked(false);
            }
        } else {
            hideCurtain();
        }
    }

    private void showConsentForm() {
        ConsentFragment.start(getSupportFragmentManager());
    }

    private void hideCurtain() {
        CurtainService.stop(this);
    }


    public void getGoogleAdId() {
        Disposable disposable = Observable.just(1).map(ignored -> {
            AdvertisingIdClient.Info idInfo = null;
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            if (idInfo != null && !TextUtils.isEmpty(idInfo.getId())) {
                PrefUtils.putString(HomeActivity.this, CommonConstants.AD_ID, idInfo.getId());
                ApplicationContextHolder.getInstance().saveAdID(idInfo.getId());
            }
            return ignored;
        }).subscribeOn(Schedulers.io()).subscribe(ignored -> {
        }, Logger::printStackTrace);
        addDisposable(disposable);
    }
}
