package com.news.shorts.views.viewholders;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.shorts.callbacks.LanguageItemSelectionCallback;
import com.news.shorts.model.Language;
import com.shorts.news.R;

import app.common.models.TypeAwareModel;
import app.common.views.BaseViewHolder;

/**
 * Created by Bhaskar on 26/7/18.
 */

public class LanguagesViewHolder extends BaseViewHolder implements View.OnClickListener {

    private final LanguageItemSelectionCallback itemClickCallback;
    private TextView title;
    private ImageView check;
    private Language language;

    public LanguagesViewHolder(View view, LanguageItemSelectionCallback itemClickCallback) {
        super(view);
        View rootConstraintLayout = view.findViewById(R.id.root_constraint);
        title = view.findViewById(R.id.txt_title);
        check = view.findViewById(R.id.iv_check);
        rootConstraintLayout.setOnClickListener(this);
        this.itemClickCallback = itemClickCallback;
    }

    public static LanguagesViewHolder create(ViewGroup parent, LanguageItemSelectionCallback itemClickCallback) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_language_card, parent, false);

        return new LanguagesViewHolder(view, itemClickCallback);
    }

    public void update(@Nullable TypeAwareModel model) {
        if (!(model instanceof Language)) {
            return;
        }
        this.language = (Language) model;
        title.setText(language.displayName);
        if(language.isSelected)
            check.setVisibility(View.VISIBLE);
        else
            check.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (itemClickCallback != null) {
            language.isSelected = true;
            itemClickCallback.onLanguageSelected(language);
        } else {
            throw new IllegalArgumentException("Caller must implement and pass LanguageItemSelectionCallback");
        }
    }
}
