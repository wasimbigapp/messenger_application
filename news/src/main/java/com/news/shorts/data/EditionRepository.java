package com.news.shorts.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.news.shorts.model.LanguageData;

import java.io.IOException;

import app.common.data.BaseRepository;
import app.common.utils.JsonUtils;
import app.common.utils.Logger;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static app.common.utils.Utils.readJsonFile;

public class EditionRepository extends BaseRepository {
    public static final String KEY = "language-config";
    @SuppressWarnings("FieldCanBeLocal")
    private final String LANGUAGE_LIST_JSON = "language-config.json";
    private MutableLiveData<LanguageData> languageData;

    public EditionRepository() {
        languageData = new MutableLiveData<>();
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchData(int offset) {
        if (languageData.getValue() == null) {
            Observable.fromCallable(this::loadLanguages)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> languageData.setValue(response), Logger::printStackTrace);
        } else {
            languageData.postValue(languageData.getValue());
        }
    }

    @NonNull
    private LanguageData loadLanguages() throws IOException {
        return JsonUtils.fromJsonToObj(readJsonFile(LANGUAGE_LIST_JSON),
                LanguageData.class);
    }

    public MutableLiveData<LanguageData> getLanguageData() {
        return languageData;
    }
}
