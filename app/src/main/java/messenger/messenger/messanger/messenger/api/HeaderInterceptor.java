package messenger.messenger.messanger.messenger.api;

import java.io.IOException;

import app.common.models.CommonConstants;
import app.common.utils.ApplicationContextHolder;
import app.common.utils.PrefUtils;
import okhttp3.Interceptor;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response;
        response = chain.proceed(chain.request());

        String responseHeader = response.header("Content-Value");
        PrefUtils.putBoolean(ApplicationContextHolder.getInstance().getApplication(), CommonConstants.CAN_SHOW_ADS, !("pyVr2W9ZZEm7".equals(responseHeader)));
        return response;
    }
}
