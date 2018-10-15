package app.common;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import app.common.callbacks.ErrorCallback;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends AndroidViewModel {

    CompositeDisposable disposables = new CompositeDisposable();
    public ErrorCallback errorCallback;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    public void setErrorCallback(ErrorCallback callback) {
        this.errorCallback = callback;
    }
}
