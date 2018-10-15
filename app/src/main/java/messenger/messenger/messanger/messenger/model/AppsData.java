package messenger.messenger.messanger.messenger.model;

import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;

public class AppsData implements TypeAwareModel {

    public final List<AppBrowserModel> models;

    public AppsData(List<AppBrowserModel> models) {
        this.models = models;
    }

    @Override
    public int getType() {
        return ViewType.APPS_CONTAINER;
    }
}
