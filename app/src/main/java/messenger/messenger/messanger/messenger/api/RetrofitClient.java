package messenger.messenger.messanger.messenger.api;


import app.common.utils.APIUtils;
import messenger.messenger.messanger.messenger.R;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static app.common.utils.Utils.getString;

/**
 * Created by bedprakash.r on 14/03/18.
 */

public class RetrofitClient {

    public static final String BIG = "BIG";
    public static final String SMALL = "SMALL";


    public static MessengerAPI getMessengerClient() {
        OkHttpClient.Builder httpClient = APIUtils.getOkHttpClientBuilder().addInterceptor(new HeaderInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MessengerAPI.class);

    }
}
