package com.news.shorts.utils;

import android.support.v7.util.DiffUtil;

import com.news.shorts.model.NewsModel;

import java.util.List;

public class NewsDiffUtil extends DiffUtil.Callback {
    private NewsDiffUtil(List<NewsModel> oldData, List<NewsModel> newData) {
        super();
    }

    @Override
    public int getOldListSize() {
        return 0;
    }

    @Override
    public int getNewListSize() {
        return 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
