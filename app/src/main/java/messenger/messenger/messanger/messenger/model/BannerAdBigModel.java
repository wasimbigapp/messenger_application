package messenger.messenger.messanger.messenger.model;

import java.io.Serializable;

import app.common.models.ViewType;

/**
 * Small Ad Model
 * Created by bedprakash.r on 06/01/18.
 */

public class BannerAdBigModel extends BannerAdModel implements Serializable {

    @ViewType
    @Override
    public int getType() {
        return ViewType.BANNER_AD_BIG;
    }
}
