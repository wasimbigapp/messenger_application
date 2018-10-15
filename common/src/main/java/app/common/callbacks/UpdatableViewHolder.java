package app.common.callbacks;

import android.support.annotation.Nullable;

import app.common.models.TypeAwareModel;

public interface UpdatableViewHolder {
    void updateData(@Nullable TypeAwareModel model);
}
