package com.news.shorts.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.shorts.news.BuildConfig;
import com.shorts.news.R;

import app.common.adapters.FragmentPayload;
import app.common.models.CommonConstants;
import app.common.utils.Utils;
import app.common.views.fragments.BaseFragment;

public class InterstitialAdFragment extends BaseFragment {
    private FragmentPayload payload;
    private InterstitialAd mInterstitialAd;

    public static Fragment create(@NonNull FragmentPayload payload) {
        Bundle args = new Bundle();
        args.putSerializable(CommonConstants.IntentKeys.FRAGMENT_PAYLOAD, payload);
        Fragment fragment = new InterstitialAdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArgs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_interstitial_fragment, container, false);
        loadInterstitalAd(getAdRequest(payload), getString(R.string.interstitial_ad_id));
        return view;
    }

    private AdRequest getAdRequest(FragmentPayload payload) {
        AdRequest.Builder adRequest = new AdRequest.Builder();
//        if (!Utils.userConsentGiven()) {
//            Utils.putNonPersonalizedData(adRequest, null);
//        }
        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice(getString(R.string.TEST_DEVICE));
        }
        return adRequest.build();
    }

    private void loadInterstitalAd(AdRequest request, String adId) {
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(adId);

        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                if (Utils.canShowAds()) {
                    mInterstitialAd.show();
                } else {
                    onAdFailedToLoad(CommonConstants.AD_FAILURE_CODE);
                }
            }
        });
        mInterstitialAd.loadAd(request);
    }

    private void extractArgs() {
        Bundle args = getArguments();
        if (args == null || !(args.getSerializable(CommonConstants.IntentKeys.FRAGMENT_PAYLOAD) instanceof FragmentPayload)) {
            throw new IllegalArgumentException("Must send FragmentPayload valid arguments");
        }

        payload = (FragmentPayload) args.getSerializable(CommonConstants.IntentKeys.FRAGMENT_PAYLOAD);
    }
}
