package app.common.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * View Types for the list
 * Created by bedprakash.r on 06/01/18.
 */

@IntDef({ViewType.BANNER_AD_SMALL, ViewType.BANNER_AD_BIG, ViewType.APP_CARD, ViewType.APP_LAUNCH_CARD,
        ViewType.EXTERNAL_APP, ViewType.BUTTON, ViewType.ANNOUNCEMENT_HEADER,
        ViewType.SMALL_ADS_CONTAINER, ViewType.BIG_ADS_CONTAINER, ViewType.APPS_CONTAINER, ViewType.NEWS_CARD_SMALL,
        ViewType.INTERSTITIAL_AD, ViewType.LANGUAGE_CARD, ViewType.NATIVE_APP_INSTALL_AD, ViewType.NATIVE_CONTENT_AD})
@Retention(RetentionPolicy.SOURCE)
public @interface ViewType {
    int BANNER_AD_SMALL = 1;
    int BANNER_AD_BIG = 2;
    int APP_CARD = 3;
    int APP_LAUNCH_CARD = 4;
    int EXTERNAL_APP = 5;
    int BUTTON = 6;
    int ANNOUNCEMENT_HEADER = 7;
    int SMALL_ADS_CONTAINER = 8;
    int BIG_ADS_CONTAINER = 9;
    int NEWS_CARD_SMALL = 10;
    int INTERSTITIAL_AD = 11;
    int APPS_CONTAINER = 12;
    int LANGUAGE_CARD = 13;
    int NATIVE_APP_INSTALL_AD = 14;
    int NATIVE_CONTENT_AD = 15;
}
