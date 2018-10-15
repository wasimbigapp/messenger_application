package app.common.callbacks;

import android.support.annotation.NonNull;

import app.common.models.ErrorInfo;

public interface ErrorCallback {
    void showError(@NonNull ErrorInfo errorInfo);

    void hideError();
}
