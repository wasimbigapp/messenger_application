package com.news.shorts.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.news.shorts.model.NewsModel;
import com.news.shorts.model.NewsPayload;
import com.news.shorts.viewmodels.NewsDetailViewModel;
import com.news.shorts.viewmodels.NewsViewModel;
import com.news.shorts.views.adapters.NewsAdapter;
import com.shorts.news.BuildConfig;
import com.shorts.news.R;

import java.util.List;

import app.common.adapters.FragmentPayload;
import app.common.callbacks.AdHoldingActivity;
import app.common.models.CommonConstants;
import app.common.models.TypeAwareModel;
import app.common.utils.PrefUtils;
import app.common.utils.Utils;
import app.common.views.activities.BaseActivity;
import timber.log.Timber;

import static app.common.utils.Utils.putNonPersonalizedData;
import static app.common.utils.Utils.userConsentGiven;
import static com.news.shorts.viewmodels.NewsDetailViewModel.INVALID_POS;

public class NewsDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener, AdHoldingActivity {

    private static final String TAG = "NewsDetailActivity";
    private ViewPager pager;
    private NewsAdapter adapter;
    private NewsDetailViewModel adapterModel;
    private NewsViewModel newsModel;
    private int startPosition;
    private InterstitialAd mInterstitialAd;
    private boolean adToBeOpened;
    private View toolTipView;

    public static void start(Context caller, NewsPayload payload, NewsModel model) {
        Intent i = new Intent(caller, NewsDetailActivity.class);
        i.putExtra(CommonConstants.IntentKeys.NEWS_PAYLOAD, payload);
        i.putExtra(CommonConstants.IntentKeys.SELECTED_ITEM, model);
        caller.startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adToBeOpened) {
            adToBeOpened = false;
            showInterstitialAd();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeToolbar();
        Timber.d(TAG + "Values: " + getIntent().getExtras());
        setContentView(R.layout.activity_news);
        pager = findViewById(R.id.news_pager);
        toolTipView = findViewById(R.id.tool_tip_view);

        adapter = new NewsAdapter(getSupportFragmentManager());
        adapterModel = ViewModelProviders.of(this).get(NewsDetailViewModel.class);
        NewsPayload payload = (NewsPayload) getIntent().getSerializableExtra(CommonConstants.IntentKeys.NEWS_PAYLOAD);
        NewsModel selectedItem = (NewsModel) getIntent().getSerializableExtra(CommonConstants.IntentKeys.SELECTED_ITEM);

        newsModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsModel.setPayload(payload);
        if (selectedItem != null) {
            adapterModel.setSelectedModel(selectedItem);
            adapterModel.getSelectedPosition().observe(this, position -> startPosition = position != null ? position : INVALID_POS);
        }

        adapterModel.loadData().observe(this, this::handleDataUpdate);

        newsModel.getModels().observe(this, this::handleNewsUdpate);

        pager.addOnPageChangeListener(this);
        pager.setAdapter(adapter);
        showBannerAd();

        if (!PrefUtils.getBoolean(this, CommonConstants.SCROLL_TIP_SHOWN, false)) {
            toolTipView.setVisibility(View.VISIBLE);
        } else {
            toolTipView.setVisibility(View.GONE);
        }

    }

    private void handleNewsUdpate(List<TypeAwareModel> typeAwareModels) {
        if (typeAwareModels != null) {
            adapterModel.updateData(typeAwareModels);
        } else {
            Utils.showErrorToast();
        }
    }

    private void handleDataUpdate(List<FragmentPayload> typeAwareModels) {
        if (typeAwareModels != null) {
            adapter.updateItems(typeAwareModels);
        }
        if (startPosition > INVALID_POS && startPosition < adapter.getCount()) {
            pager.setCurrentItem(startPosition);
            startPosition = INVALID_POS;
        }
    }

    private void showBannerAd() {
        AdView mAdView = findViewById(R.id.ad_view_news_details);
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


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        adapterModel.onPageSelected(position);
        newsModel.onScrollChanged(1, adapter.getCount(), position);
        newsModel.setCurrentPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (ViewPager.SCROLL_STATE_DRAGGING == state
                && View.VISIBLE == toolTipView.getVisibility()) {
            PrefUtils.putBoolean(this, CommonConstants.SCROLL_TIP_SHOWN, true);
            toolTipView.setVisibility(View.GONE);
        }

    }


    private void showInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            if (Utils.canShowAds()) {
                mInterstitialAd.show();
            }
        }
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

    private AdRequest getInterstitialAdRequest() {
        AdRequest.Builder adRequest = new AdRequest.Builder();
        if (!Utils.userConsentGiven()) {
            Utils.putNonPersonalizedData(adRequest, null);
        }
        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice(getString(R.string.TEST_DEVICE));
        }
        return adRequest.build();

    }

    @Override
    public void showAdOnResume() {
        adToBeOpened = true;
        loadInterstitialAd();
    }
}
