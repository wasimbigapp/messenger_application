package messenger.messenger.messanger.messenger.api;


import java.util.ArrayList;

import io.reactivex.Observable;
import messenger.messenger.messanger.messenger.model.AppBrowserModel;
import messenger.messenger.messanger.messenger.model.BannerAdBigModel;
import messenger.messenger.messanger.messenger.model.BannerAdSmallModel;
import messenger.messenger.messanger.messenger.model.CardScreenType;
import messenger.messenger.messanger.messenger.model.ExternalAppModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by bedprakash.r on 14/03/18.
 */

public interface MessengerAPI {
    @GET("api/bannerad/")
    Call<ArrayList<BannerAdSmallModel>> getSmallAds(@Query("type") String type);

    @GET("api/bannerad/")
    Observable<ArrayList<BannerAdSmallModel>> getSmallAdsRx(@Query("type") String type);

    @GET("api/bannerad/")
    Call<ArrayList<BannerAdBigModel>> getBigAds(@Query("type") String type);

    @GET("api/bannerad/")
    Observable<ArrayList<BannerAdBigModel>> getBigAdsRx(@Query("type") String type);

    @GET("api/adbroswer/")
    Call<ArrayList<ExternalAppModel>> getAppCardForLandingScreen(@Query("screen") @CardScreenType String type);


    @GET("api/adbroswer/")
    Call<ArrayList<AppBrowserModel>> getAppCards(@Query("screen") @CardScreenType String type);

    @GET("api/adbroswer/")
    Observable<ArrayList<AppBrowserModel>> getAppCardsRx(@Query("screen") @CardScreenType String type);
}
