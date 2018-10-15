package messenger.messenger.messanger.messenger.model;

import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;

public class SmallAdsData implements TypeAwareModel {

    public final List<BannerAdSmallModel> models;
    public final int position;

    public SmallAdsData(List<BannerAdSmallModel> models, int position) {
        this.models = models;
        this.position = position;
    }

    @Override
    public int getType() {
        return ViewType.SMALL_ADS_CONTAINER;
    }
}
