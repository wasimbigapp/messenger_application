package com.news.shorts.views.adapters;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.news.shorts.callbacks.LanguageItemSelectionCallback;
import com.news.shorts.views.viewholders.LanguagesViewHolder;

import app.common.adapters.ListAdapter;
import app.common.models.ViewType;

/**
 * Created by Bhaskar on 26/7/18.
 */
public class LanguageListAdapter extends ListAdapter {

    private LanguageItemSelectionCallback itemClickCallback;

    @Override
    @CallSuper
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ViewType.LANGUAGE_CARD: {
                return LanguagesViewHolder.create(parent, itemClickCallback);
            }
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    public void setClickCallback(LanguageItemSelectionCallback callback) {
        this.itemClickCallback = callback;
    }
}
