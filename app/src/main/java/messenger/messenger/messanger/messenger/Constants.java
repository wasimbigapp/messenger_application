package messenger.messenger.messanger.messenger;

import static app.common.utils.Utils.getString;

/**
 * Class to hold CommonConstants
 * Created by bedprakash.r on 07/01/18.
 */

public interface Constants {
    String APPS_COLLECTION_NAME = "Apps";
    String BIG_ADS_COLLECTION_NAME = "bigAds";
    String SMALL_ADS_COLLECTION_NAME = "smallAds";
    String APPS_LIST_JSON = "applist.json";
    String SCHEME_SEPARATOR = "://";
    String FACEBOOK_URL = "http://www.facebook.com";
    String SALE_URL = "http://tiny.cc/shop50";
    String APP_RATE_DONE = "app_rate_done";
    String APP_LAUNCH_COUNT = "launch_count";

    int APP_LOADER = 101;
    int STICKY_NOTIFICATION_ID = 102;
    int REQ_CODE_NOTIFICATION = 103;
    String IS_NOTIFICATION_ENABLED = "notificationEnabled";
    String MESSENGER_NOTIFICATION_CHANNEL_ID = AppUtils.getPackageName();
    String MESSENGER_NOTIFICATION_CHANNEL_Name = getString(R.string.app_name);
    String VIEW_TYPE = "viewOrder";
    String CURTAIN_HEIGHT_PREF = "curtain_height_pref";
    String FORWARD_ACTION = "forward_action";
    String ACTION_COLOR_PICKER = AppUtils.getPackageName() + ".colorPickerOpen";
    String CURTAIN_CONFIG = "curtainConfig";
    String SMALL_AD_POSITION = "small_ad_position";
    String BIG_AD_POSITION = "big_ad_position";
}
