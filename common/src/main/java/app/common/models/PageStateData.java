package app.common.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.common.data.PageState;

public class PageStateData {
    public final PageState state;
    private ErrorInfo error;

    public PageStateData(@NonNull PageState state) {
        this.state = state;
    }

    public PageStateData(@NonNull PageState state, ErrorInfo error) {
        this.state = state;
        this.error = error;
    }

    @Nullable
    public ErrorInfo getError() {
        return error;
    }

    @Override
    public String toString() {
        return "PageStateData{" +
                "state=" + state +
                ", error=" + error +
                '}';
    }
}
