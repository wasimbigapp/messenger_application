package com.news.shorts.model;

import java.io.Serializable;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;

public class Language implements TypeAwareModel, Serializable {
    public int type = ViewType.LANGUAGE_CARD;
    public String languageCode;
    public String displayName;
    public boolean isSelected;

    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public int getType() {
        return type;
    }
}
