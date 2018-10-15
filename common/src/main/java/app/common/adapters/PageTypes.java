package app.common.adapters;

import android.support.annotation.StringDef;

import static app.common.adapters.PageTypes.FAST_BROWSING;
import static app.common.adapters.PageTypes.FEATURED;
import static app.common.adapters.PageTypes.GAMING_APPS;
import static app.common.adapters.PageTypes.INTERSTITIAL_AD;
import static app.common.adapters.PageTypes.NEWS;
import static app.common.adapters.PageTypes.NEWS_DETAIL;
import static app.common.adapters.PageTypes.SOCIAL_APPS;
import static app.common.adapters.PageTypes.TOP_USED_APPS;

/**
 * Created by bedprakash.r on 05/06/18.
 */

@StringDef({FEATURED, FAST_BROWSING, NEWS, NEWS_DETAIL, INTERSTITIAL_AD,TOP_USED_APPS,SOCIAL_APPS,GAMING_APPS})
public @interface PageTypes {
    String FEATURED = "FEATURED APPS";
    String FAST_BROWSING = "FAST BROWSING";
    String NEWS = "FEEDS";
    String NEWS_DETAIL = "news_detail";
    String INTERSTITIAL_AD = "interstitial_ad";
    String TOP_USED_APPS = "TOP USED APPS";
    String SOCIAL_APPS = "SOCIAL APPS";
    String GAMING_APPS = "GAMING APPS";
}
