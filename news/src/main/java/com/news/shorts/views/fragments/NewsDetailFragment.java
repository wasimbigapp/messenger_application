package com.news.shorts.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.shorts.model.NewsModel;
import com.shorts.news.R;
import com.squareup.picasso.Picasso;

import app.common.adapters.FragmentPayload;
import app.common.callbacks.AdHoldingActivity;
import app.common.models.CommonConstants;
import app.common.models.TypeAwareModel;
import app.common.utils.Utils;
import app.common.views.fragments.BaseFragment;
import bpr10.git.crhometabs.chrometabs.ChromeUtils;

public class NewsDetailFragment extends BaseFragment implements View.OnClickListener {

    private View fullStory;
    private TextView title;
    private TextView source;
    private TextView description;
    private ImageView image;

    private FragmentPayload payload;

    private NewsModel newsModel;
    private boolean adToBeOpened;

    public static Fragment create(FragmentPayload payload) {
        Bundle args = new Bundle();
        args.putSerializable(CommonConstants.IntentKeys.FRAGMENT_PAYLOAD, payload);
        Fragment fragment = new NewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArgs();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_news_detail, container, false);

        title = view.findViewById(R.id.txt_title);
        description = view.findViewById(R.id.txt_description);
        image = view.findViewById(R.id.img_news);
        source = view.findViewById(R.id.txt_source);
        fullStory = view.findViewById(R.id.full_story);

        if (payload != null && payload.getParams() != null && payload.getParams().get(CommonConstants.MODEL) instanceof NewsModel) {
            NewsModel newsData = (NewsModel) payload.getParams().get(CommonConstants.MODEL);
            update(newsData);
        }


        return view;
    }

    private void extractArgs() {
        Bundle args = getArguments();
        if (args == null || !(args.getSerializable(CommonConstants.IntentKeys.FRAGMENT_PAYLOAD) instanceof FragmentPayload)) {
            throw new IllegalArgumentException("Must send FragmentPayload valid arguments");
        }

        payload = (FragmentPayload) args.getSerializable(CommonConstants.IntentKeys.FRAGMENT_PAYLOAD);
    }

    public void update(@Nullable TypeAwareModel model) {
        if (!(model instanceof NewsModel)) {
            return;
        }
        NewsModel newsModel = (NewsModel) model;
        this.newsModel = newsModel;
        title.setText(newsModel.title);
        description.setText(newsModel.summary);
        String sourceText = newsModel.contentSource + " . " + Utils.getReadableTime(newsModel.publishedAt) + getString(R.string.ago);
        source.setText(sourceText);
        Picasso.with(image.getContext()).load(newsModel.images.mainImage.url).tag(this).into(image);
        fullStory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.full_story) {
            if (newsModel != null) {
                adToBeOpened = false;
                if (getActivity() instanceof AdHoldingActivity) {
                    ((AdHoldingActivity) getActivity()).showAdOnResume();
                }
                ChromeUtils.openInExternalBrowser(getContext(), newsModel.contentURL);
            }
        }
    }
}
