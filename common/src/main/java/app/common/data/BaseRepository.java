package app.common.data;

import android.arch.lifecycle.MutableLiveData;

import app.common.models.PageStateData;

public abstract class BaseRepository {
    protected MutableLiveData<PageStateData> pageState;

    public boolean isLoading() {
        return pageState.getValue() != null && PageState.LOADING == pageState.getValue().state;
    }

    public MutableLiveData<PageStateData> getPageState() {
        return pageState;
    }

    public abstract void fetchData(int offset);

}
