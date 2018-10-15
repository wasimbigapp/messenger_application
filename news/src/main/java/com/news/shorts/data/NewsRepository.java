package com.news.shorts.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.news.shorts.api.ApiClient;
import com.news.shorts.model.NewsPayload;

import java.util.ArrayList;
import java.util.List;

import app.common.data.BaseRepository;
import app.common.data.PageState;
import app.common.models.ErrorInfo;
import app.common.models.PageStateData;
import app.common.models.TypeAwareModel;
import app.common.utils.Logger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository extends BaseRepository {
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_FETCH_LIMIT = 3;

    private final NewsPayload newsPayload;
    private MutableLiveData<List<TypeAwareModel>> models;

    public NewsRepository(@NonNull NewsPayload newsPayload) {
        this.newsPayload = newsPayload;
        models = new MutableLiveData<>();
        pageState = new MutableLiveData<>();
    }

    @SuppressLint("CheckResult")
    public void fetchData(int offset) {
        if (isLoading()) {
            return;
        }

        if (models.getValue() != null && models.getValue().size() - offset > PAGE_FETCH_LIMIT) {
            return;
        }

        List<TypeAwareModel> initialData = models.getValue();
        if (initialData == null) {
            initialData = new ArrayList<>(0);
        }

        ApiClient.getMessengerClient()
                .getNewsVernacular(getCurrentOffset(), PAGE_SIZE, newsPayload.languageCode)
                .subscribeOn(Schedulers.io())
                .map(celtikResponse -> celtikResponse.content)
                .scan(initialData, (oldData, newData) -> {
                    oldData.addAll(newData);
                    return oldData;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::updateModels)
                .doOnSubscribe(d -> pageState.setValue(new PageStateData(PageState.LOADING)))
                .doOnComplete(() -> pageState.setValue(new PageStateData(PageState.LOADED)))
                .doOnError(ignored -> pageState.setValue(new PageStateData(PageState.ERROR, new ErrorInfo())))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                }, Logger::printStackTrace);
    }

    public void updateModels(@NonNull List<TypeAwareModel> newData) {
        models.setValue(newData);
    }


    public int getCurrentOffset() {
        return models.getValue() != null ? models.getValue().size() : 0;
    }

    public LiveData<List<TypeAwareModel>> getModels() {
        return models;
    }
}
