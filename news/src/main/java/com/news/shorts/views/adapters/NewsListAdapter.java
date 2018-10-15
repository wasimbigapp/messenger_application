package com.news.shorts.views.adapters;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.news.shorts.views.viewholders.NativeAppInstallAdViewHolder;
import com.news.shorts.views.viewholders.NewsViewHolder;

import app.common.adapters.ListAdapter;
import app.common.data.NewsClickCallback;
import app.common.models.ViewType;

/**
 * Created by bedprakash.r on 13/06/18.
 */

public class NewsListAdapter extends ListAdapter {

    private NewsClickCallback newsClickCallback;

    @Override
    @CallSuper
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ViewType.NEWS_CARD_SMALL: {
                return NewsViewHolder.create(parent, newsClickCallback);
            }
            case ViewType.NATIVE_APP_INSTALL_AD: {
                return NativeAppInstallAdViewHolder.create(parent);
            }
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    public void setClickCallback(NewsClickCallback callback) {
        this.newsClickCallback = callback;
    }
}
