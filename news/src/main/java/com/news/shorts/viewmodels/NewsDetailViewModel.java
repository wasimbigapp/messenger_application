package com.news.shorts.viewmodels;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.news.shorts.model.NewsModel;
import com.shorts.news.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.common.BaseViewModel;
import app.common.adapters.FragmentPayload;
import app.common.adapters.PageTypes;
import app.common.models.CommonConstants;
import app.common.models.TypeAwareModel;
import app.common.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static app.common.utils.Utils.getString;

public class NewsDetailViewModel extends BaseViewModel {

    public static final int INVALID_POS = -1;
    private MutableLiveData<List<FragmentPayload>> models;
    private MutableLiveData<Integer> selectedPosition;
    @SuppressWarnings("FieldCanBeLocal")
    private int AD_GAP = 5;
    private InterstitialAd mInterstitialAd;
    private int lastAdPosition = -1;
    private NewsModel selectedModel;
    private boolean hasSelectedPosition;
    private int startPosition = INVALID_POS;

    public NewsDetailViewModel(@NonNull Application application) {
        super(application);
        models = new MutableLiveData<>();
        selectedPosition = new MutableLiveData<>();
        loadInterstitalAd(Utils.getAdRequestData(), getString(R.string.interstitial_ad_id));
    }

    public void setSelectedModel(@NonNull NewsModel model) {
        this.selectedModel = model;
    }

    public MutableLiveData<List<FragmentPayload>> loadData() {
        return models;
    }

    public void updateData(@NonNull List<TypeAwareModel> newData) {
        Disposable computationDisposable = Observable.fromCallable(() -> updateAdsDataAndConvertModel(newData))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(results -> {
                    if (startPosition > INVALID_POS) {
                        selectedPosition.setValue(startPosition);
                        startPosition = INVALID_POS;
                    }
                    // This order is important for the view pager to set current item
                    models.setValue(results);
                }, error -> Utils.showErrorToast());
        addDisposable(computationDisposable);
    }

    private List<FragmentPayload> updateAdsDataAndConvertModel(@NonNull List<TypeAwareModel> newData) {
        List<FragmentPayload> updateModels = new ArrayList<>();
        for (int i = 0; i < newData.size(); i++) {


            TypeAwareModel model = newData.get(i);
            if (!(model instanceof NewsModel)) {
                continue;
            }
            Map<String, TypeAwareModel> params = new HashMap<>();
            if (!hasSelectedPosition && selectedModel != null && TextUtils.equals(selectedModel.contentId, ((NewsModel) model).contentId)) {
                hasSelectedPosition = true;
                startPosition = i;
            }

            params.put(CommonConstants.MODEL, model);

            FragmentPayload payload = new FragmentPayload(PageTypes.NEWS_DETAIL, null, params);
            updateModels.add(payload);
        }

        return updateModels;
    }

    public void onPageSelected(int position) {
        if (position != -1
                && ((lastAdPosition - position) % AD_GAP == 0)) {
            lastAdPosition = position;
            if (mInterstitialAd.isLoaded()) {
                if (Utils.canShowAds()) {
                    mInterstitialAd.show();
                }
            }
        }
    }

    private void loadInterstitalAd(AdRequest request, String adId) {
        mInterstitialAd = new InterstitialAd(getApplication());
        mInterstitialAd.setAdUnitId(adId);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                loadInterstitalAd(Utils.getAdRequestData(), getString(R.string.interstitial_ad_id));
            }
        });
        mInterstitialAd.loadAd(request);
    }

    public MutableLiveData<Integer> getSelectedPosition() {
        return selectedPosition;
    }
}
