package app.common.utils;

import android.support.annotation.NonNull;

import app.common.models.ErrorInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bedprakash.r on 14/03/18.
 */

public abstract class RetrofitCallbackWrapper<T> implements Callback<T> {

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
            onSuccess(response.body());
        } else {
            onError(new ErrorInfo());
        }
    }


    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onError(new ErrorInfo());
    }

    public abstract void onSuccess(@NonNull T body);

    public abstract void onError(@NonNull ErrorInfo errorInfo);
}
