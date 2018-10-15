package com.news.shorts.model;

import java.io.Serializable;
import java.util.List;

public class Country implements Serializable {
    public List<Language> languages;
    public String displayName;
    public String countryCode;
    public boolean isSelected;

    @Override
    public String toString() {
        return displayName;
    }
}
