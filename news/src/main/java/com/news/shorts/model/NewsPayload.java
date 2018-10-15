package com.news.shorts.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class NewsPayload implements Serializable {
    @NonNull
    public String languageCode;
    public int offset;

    public NewsPayload(@NonNull String languageCode, int offset) {
        this.languageCode = languageCode;
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewsPayload)) {
            return false;
        }
        NewsPayload that = (NewsPayload) o;
        return languageCode != null && languageCode.equals(that.languageCode);
    }

    @Override
    public int hashCode() {
        return languageCode == null ? super.hashCode() : languageCode.hashCode();
    }
}
