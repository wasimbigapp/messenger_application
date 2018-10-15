package messenger.messenger.messanger.messenger.model;

import app.common.models.ViewType;

/**
 * Small Ad Model
 * Created by bedprakash.r on 06/01/18.
 */

public class BannerAdSmallModel extends BannerAdModel {

    @ViewType
    @Override
    public int getType() {
        return ViewType.BANNER_AD_SMALL;
    }
}
