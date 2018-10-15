package app.common.data;

import app.common.models.TypeAwareModel;

public interface NewsClickCallback {
    void onClick(TypeAwareModel model, int position);
}
