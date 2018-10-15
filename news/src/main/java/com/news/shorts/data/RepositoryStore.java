package com.news.shorts.data;

import android.support.annotation.NonNull;

import com.news.shorts.model.NewsPayload;

import java.util.HashMap;
import java.util.Map;

import app.common.data.BaseRepository;

public class RepositoryStore {
    private static RepositoryStore sInstance;
    private Map<Object, BaseRepository> store;


    private RepositoryStore() {
        store = new HashMap<>();
    }

    public static RepositoryStore getInstance() {
        if (sInstance == null) {
            synchronized (RepositoryStore.class) {
                if (sInstance == null) {
                    sInstance = new RepositoryStore();
                }
            }
        }

        return sInstance;
    }

    @NonNull
    public BaseRepository getRepository(Object key) {
        BaseRepository repository = store.get(key);

        if (repository != null) {
            return repository;
        } else if (key instanceof NewsPayload) {
            repository = new NewsRepository((NewsPayload) key);
            store.put(key, repository);
        } else if (EditionRepository.KEY.equals(key)) {
            repository = new EditionRepository();
            store.put(key, repository);
        } else {
            throw new IllegalArgumentException("Not handled key " + key);
        }
        return repository;
    }


}
