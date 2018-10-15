package messenger.messenger.messanger.messenger;


import java.util.ArrayList;

import messenger.messenger.messanger.messenger.api.RetrofitClient;
import messenger.messenger.messanger.messenger.model.AppBrowserModel;
import messenger.messenger.messanger.messenger.model.BannerAdBigModel;
import messenger.messenger.messanger.messenger.model.BannerAdSmallModel;
import messenger.messenger.messanger.messenger.model.CardScreenType;
import retrofit2.Callback;

import static messenger.messenger.messanger.messenger.api.RetrofitClient.BIG;
import static messenger.messenger.messanger.messenger.api.RetrofitClient.SMALL;

/**
 * Singleton for DB Access
 * <p>
 * Created by bedprakash.r on 06/01/18.
 */

public class FirebaseDBHelper {

    private static final String TAG = "FirebaseDBHelper";
    private static FirebaseDBHelper sInstance;

    private FirebaseDBHelper() {

    }

    public static FirebaseDBHelper getFireBaseDb() {
        if (sInstance == null) {
            synchronized (FirebaseDBHelper.class) {
                if (sInstance == null) {
                    sInstance = new FirebaseDBHelper();
                }
            }
        }
        return sInstance;
    }

    public void fetchApps(Callback<ArrayList<AppBrowserModel>> callback) {
        RetrofitClient.getMessengerClient().getAppCards(CardScreenType.APP_LIST).enqueue(callback);
    }

    public void fetchBigAds(Callback<ArrayList<BannerAdBigModel>> callback) {
        RetrofitClient.getMessengerClient().getBigAds(BIG).enqueue(callback);
    }
}
