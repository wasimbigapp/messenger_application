package app.common.models;

/**
 * Created by bedprakash.r on 13/06/18.
 */

public interface CommonConstants {
    String AD_ID = "AD_ID";
    String MODEL = "MODEL";
    String CAN_SHOW_ADS = "can_show_ads";
    String IS_REQUEST_LOCATION_IN_EEA_OR_UNKNOWN = "is_request_location_in_eea_or_unknown";
    String IS_CONSENT_GIVEN = "is_consent_given";
    String SCROLL_TIP_SHOWN = "scroll_tip_shown";
    int AD_FAILURE_CODE = 1;
    String EN_CODE = "en";

    interface IntentKeys {
        String FRAGMENT_PAYLOAD = "FRAGMENT_PAYLOAD";
        String NEWS_PAYLOAD = "NEWS_PAYLOAD";
        String SELECTED_ITEM = "selectedItem";
    }
}
