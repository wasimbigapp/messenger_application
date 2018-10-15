package com.news.shorts.api;

import com.news.shorts.model.CeltikResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("content")
    Observable<CeltikResponse> getNews(@Query("offset") int offset, @Query("limit") int limit);

    @GET("content")
    Observable<CeltikResponse> getNewsVernacular(@Query("offset") int offset, @Query("limit") int limit, @Query("language") String languageCode);

    @GET("content")
    Call<CeltikResponse> getNewsCall(@Query("offset") int offset, @Query("limit") int limit);
}
