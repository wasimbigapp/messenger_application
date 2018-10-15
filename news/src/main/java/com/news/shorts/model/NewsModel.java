package com.news.shorts.model;

import java.io.Serializable;
import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;

public class NewsModel implements TypeAwareModel, Serializable {
    public int type = ViewType.NEWS_CARD_SMALL;
    public String contentId;
    public String title;
    public String summary;
    public String contentSource;
    public List<String> categories = null;
    public List<String> categoriesEnglish = null;
    public Images images;
    public List<String> countries = null;
    public String locale;
    public int publishedAt;
    public int views;
    public Boolean isPreview;
    public String contentURL;
    public String recommendationId;

    @Override
    public int getType() {
        return type;
    }


    @Override
    public String toString() {
        return "NewsModel{" +
                "contentId='" + contentId + '\'' +
                '}';
    }
}


