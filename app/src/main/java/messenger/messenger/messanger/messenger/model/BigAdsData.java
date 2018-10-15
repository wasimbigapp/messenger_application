package messenger.messenger.messanger.messenger.model;

import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;

public class BigAdsData implements TypeAwareModel {

    public final List<BannerAdBigModel> models;
    public final int position;

    public BigAdsData(List<BannerAdBigModel> models, int position) {
        this.models = models;
        this.position = position;
    }

    @Override
    public int getType() {
        return ViewType.BIG_ADS_CONTAINER;
    }
}
