package com.news.shorts.api;

import app.common.utils.APIUtils;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static NewsApi getMessengerClient() {
        OkHttpClient.Builder httpClient = APIUtils.getOkHttpClientBuilder();

        httpClient.addInterceptor(new CeltikAPIInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://contentapi.celltick.com/mediaApi/v1.0/")
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(NewsApi.class);
    }

}
