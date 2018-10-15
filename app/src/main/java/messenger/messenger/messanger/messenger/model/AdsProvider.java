package messenger.messenger.messanger.messenger.model;

import java.util.List;

/**
 * Created by bedprakash.r on 15/01/18.
 */

public interface AdsProvider {
    List<BannerAdSmallModel> getSmallAds();

    List<BannerAdBigModel> getBigAds();
}
