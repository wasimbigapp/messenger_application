package com.news.shorts.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.news.shorts.data.NewsRepository;
import com.news.shorts.data.RepositoryStore;
import com.news.shorts.model.NewsPayload;

import java.util.List;

import app.common.BaseViewModel;
import app.common.models.PageStateData;
import app.common.models.TypeAwareModel;
import hugo.weaving.DebugLog;

/**
 * Created by bedprakash.r on 13/06/18.
 */

public class NewsViewModel extends BaseViewModel {
    private static final String TAG = "NewsViewModel";
    private LiveData<List<TypeAwareModel>> models;
    private LiveData<PageStateData> pageState;
    private int currentPosition;
    private MutableLiveData<NewsPayload> payloadLiveData;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        payloadLiveData = new MutableLiveData<>();
        models = Transformations.switchMap(payloadLiveData, input -> {
            NewsRepository newsRepo = (NewsRepository) RepositoryStore.getInstance().getRepository(input);
            newsRepo.fetchData(input.offset);
            return newsRepo.getModels();
        });

        pageState = Transformations.switchMap(payloadLiveData, input -> {
            NewsRepository newsRepo = (NewsRepository) RepositoryStore.getInstance().getRepository(input);
            return newsRepo.getPageState();
        });

    }

    @DebugLog
    public void loadData(@Nullable List<TypeAwareModel> oldModels) {
        NewsPayload payload = payloadLiveData.getValue();
        if (payload == null) {
            payload = new NewsPayload("hi", 0);
        }
        payload.offset = oldModels == null ? 0 : oldModels.size();
        payloadLiveData.setValue(payload);
    }

    public void onScrollChanged(int visibleItemCount, int totalItemCount, int firstVisibleItemPosition) {
        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0) {
            Log.v(TAG, "Fetch Page :" + pageState.getValue());
            loadData(models.getValue());
        }
    }

    @DebugLog
    public int getCurrentPosition() {
        return currentPosition;
    }

    @DebugLog
    public void setCurrentPosition(int position) {
        currentPosition = position;
    }

    public LiveData<List<TypeAwareModel>> getModels() {
        return models;
    }

    public LiveData<PageStateData> getPageState() {
        return pageState;
    }


    public void setPayload(@NonNull NewsPayload payload) {
        this.payloadLiveData.setValue(payload);
    }
}
