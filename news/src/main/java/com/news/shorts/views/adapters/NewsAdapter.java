package com.news.shorts.views.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.news.shorts.views.fragments.InterstitialAdFragment;
import com.news.shorts.views.fragments.NewsDetailFragment;

import org.jetbrains.annotations.NotNull;

import app.common.adapters.FragmentAdapter;
import app.common.adapters.FragmentPayload;
import app.common.adapters.PageTypes;


/**
 * Adapter to home screen pager
 * Created by bedprakash.r on 06/01/18.
 */
public class NewsAdapter extends FragmentAdapter {

    public NewsAdapter(FragmentManager fm) {
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getPagerItem(@NonNull FragmentPayload payload) {
        switch (payload.getPageType()) {

            case PageTypes.NEWS_DETAIL: {
                return NewsDetailFragment.create(payload);
            }
            case PageTypes.INTERSTITIAL_AD: {
                return InterstitialAdFragment.create(payload);
            }
            default:
                return super.getPagerItem(payload);
        }
    }
}

