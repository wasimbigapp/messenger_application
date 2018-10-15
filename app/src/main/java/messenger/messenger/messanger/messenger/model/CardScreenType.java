package messenger.messenger.messanger.messenger.model;

import android.support.annotation.StringDef;

/**
 * Created by bedprakash.r on 17/03/18.
 */

@StringDef({CardScreenType.MAIN_SCREEN, CardScreenType.APP_LIST})
public @interface CardScreenType {
    String MAIN_SCREEN = "MAIN_SCREEN";
    String APP_LIST = "APP_LIST";
}