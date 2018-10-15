package com.news.shorts.api;

import android.net.Uri;
import android.text.TextUtils;

import com.news.shorts.utils.NewsConstants;

import java.io.IOException;

import app.common.models.CommonConstants;
import app.common.utils.ApplicationContextHolder;
import app.common.utils.PrefUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class CeltikAPIInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Uri url = Uri.parse(request.url().toString())
                .buildUpon()
                .appendQueryParameter(NewsConstants.PARAMETER_PUBLISHER_ID, "TheMessengerApp-Web")
                .appendQueryParameter(NewsConstants.PARAMETER_PUBLISHER_KEY, "gzB6eaVCadjvGXevQs1TIVpjonTImhp1")
                .build();

        String adID = PrefUtils.getString(ApplicationContextHolder.getInstance().getApplication(), CommonConstants.AD_ID);
        if (!TextUtils.isEmpty(adID)) {
            url = url.buildUpon()
                    .appendQueryParameter("aid", adID)
                    .appendQueryParameter("userId", adID)
                    .build();
        } else {
            url = url.buildUpon()
                    .appendQueryParameter("userId", "123e-3345-6678-7777")
                    .appendQueryParameter("aid", "8edf3d65-75bf-438b-ba7d-2766e398d2ca")
                    .build();
        }

        Request updatedRequest = request.newBuilder().url(url.toString()).build();

        return chain.proceed(updatedRequest);
    }
}
