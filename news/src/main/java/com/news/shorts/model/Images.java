package com.news.shorts.model;

import java.io.Serializable;
import java.util.List;

public class Images implements Serializable {
    public Image mainImage;
    public MainImageThumbnail mainImageThumbnail;
    public List<Image> additionalImages;
}