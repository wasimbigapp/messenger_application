package com.news.shorts.views.viewholders;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.shorts.model.NewsModel;
import com.shorts.news.R;
import com.squareup.picasso.Picasso;

import app.common.data.NewsClickCallback;
import app.common.models.TypeAwareModel;
import app.common.utils.Utils;
import app.common.views.BaseViewHolder;

import static app.common.utils.Utils.getString;

/**
 * Created by bedprakash.r on 13/06/18.
 */

public class NewsViewHolder extends BaseViewHolder implements View.OnClickListener {

    private final NewsClickCallback newsClickCallback;
    private TextView title;
    private ImageView image;
    private TextView source;
    private ConstraintLayout rootConstraintLayout;
    private NewsModel newsModel;

    public NewsViewHolder(View view, NewsClickCallback newsClickCallback) {
        super(view);
        rootConstraintLayout = view.findViewById(R.id.root_constraint);
        title = view.findViewById(R.id.txt_title);
        source = view.findViewById(R.id.txt_source);
        image = view.findViewById(R.id.img_news);

        rootConstraintLayout.setOnClickListener(this);
        this.newsClickCallback = newsClickCallback;
    }

    public static NewsViewHolder create(ViewGroup parent, NewsClickCallback newsClickCallback) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_card, parent, false);

        return new NewsViewHolder(view, newsClickCallback);
    }

    public void update(@Nullable TypeAwareModel model) {
        if (!(model instanceof NewsModel)) {
            return;
        }
        this.newsModel = (NewsModel) model;
        title.setText(newsModel.title);

        String source = newsModel.contentSource + " . " + Utils.getReadableTime(newsModel.publishedAt) + getString(R.string.ago);

        this.source.setText(source);

        ConstraintSet set = new ConstraintSet();
        set.clone(rootConstraintLayout);
        set.setDimensionRatio(image.getId(), "H,1:" + (float) newsModel.images.mainImage.height / (float) newsModel.images.mainImage.width);
        set.applyTo(rootConstraintLayout);

        Picasso.with(image.getContext()).load(newsModel.images.mainImage.url).tag(this).into(image);

    }

    @Override
    public void onClick(View view) {
        if (newsClickCallback != null) {
            newsClickCallback.onClick(newsModel, getAdapterPosition());
        } else {
            throw new IllegalArgumentException("Caller must implement and pass NewsClickCallback");
        }
    }
}
