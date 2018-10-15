package messenger.messenger.messanger.messenger.model;

import android.support.annotation.NonNull;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;

public class HeaderData implements TypeAwareModel {

    @NonNull
    public String headerTitle;

    @Override
    public int getType() {
        return ViewType.ANNOUNCEMENT_HEADER;
    }
}
