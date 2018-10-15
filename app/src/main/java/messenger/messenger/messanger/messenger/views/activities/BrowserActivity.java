package messenger.messenger.messanger.messenger.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import app.common.utils.Utils;
import app.common.views.activities.BaseActivity;
import messenger.messenger.messanger.messenger.BuildConfig;
import messenger.messenger.messanger.messenger.R;

import static app.common.utils.Utils.putNonPersonalizedData;
import static app.common.utils.Utils.userConsentGiven;

public class BrowserActivity extends BaseActivity {

    public static final String KEY_SHOW_AD = "showAd";
    private static final String KEY_URL = "url";
    private static final String TAG = "BrowserActivity";
    private WebView webView;
    private ProgressBar progressBar;

    public static void launch(@NonNull Context context, @NonNull String url) {
        context.startActivity(getLaunchIntent(context, url, false));
    }

    @NonNull
    public static Intent getLaunchIntent(@NonNull Context context, @NonNull String url, boolean showAd) {
        Intent i = new Intent(context, BrowserActivity.class);
        i.putExtra(KEY_URL, url);
        i.putExtra(KEY_SHOW_AD, showAd);
        return i;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_browser);
        progressBar = findViewById(R.id.pb_webview);
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);

        String url = getIntent().getStringExtra(KEY_URL);
        if (url == null) {
            Log.e("%s Blank Url ", TAG);
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        webView = findViewById(R.id.webview);
        WebSettings setting = webView.getSettings();
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDefaultTextEncodingName("UTF-8");
        setting.setSupportZoom(false);
        setting.setBuiltInZoomControls(false);
        setting.setUseWideViewPort(true);
        setting.setDomStorageEnabled(true);
        setting.setAppCacheMaxSize(8 * 1024 * 1024);
        setting.setAllowFileAccess(true);
        setting.setAppCacheEnabled(true);
        setting.setDatabaseEnabled(true);
        setting.setLightTouchEnabled(true);
        setting.setSavePassword(false);
        setting.setSaveFormData(false);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        setting.setDatabasePath(dir);
        setting.setGeolocationDatabasePath(dir);
        setting.setGeolocationEnabled(true);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(url);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setTitle(view.getTitle());
            }
        });

        webView.loadUrl(url);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        if (isShowingAds()) {
            showBannerAd();
        } else {
//            findViewById(R.id.adView).setVisibility(View.GONE);
        }

    }

    private void showBannerAd() {
        AdView mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest.Builder adRequest = new AdRequest.Builder();

        if (!userConsentGiven()) {
            putNonPersonalizedData(adRequest, null);
        }

        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice(app.common.utils.Utils.getString(R.string.TEST_DEVICE));
        }
        if (Utils.canShowAds()) {
            mAdView.loadAd(adRequest.build());
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }

        if (isShowingAds()) {
            Intent i = new Intent(getPackageName() + ".splash");
            startActivity(i);
        }

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isShowingAds() {
        return getIntent().getBooleanExtra(KEY_SHOW_AD, false);
    }
}
