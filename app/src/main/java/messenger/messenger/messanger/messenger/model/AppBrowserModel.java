package messenger.messenger.messanger.messenger.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;

/**
 * Created by bedprakash.r on 06/01/18.
 */

public class AppBrowserModel extends BaseItem implements UniqueEntity, TypeAwareModel, Serializable {

    @SerializedName("app_name")
    public String appName;

    @SerializedName("package_name")
    public String packageName;

    @SerializedName("landing_url")
    public String landingUrl;

    @SerializedName("icon_url")
    public String iconUrl;

    // mainScreen, appList
    @SerializedName("screen")
    public String screenType;

    @SerializedName("view_order")
    public int viewOrder = 0;

    @Override
    public String getId() {
        return id;
    }

    @ViewType
    @Override
    public int getType() {
        return ViewType.APP_CARD;
    }

    @Override
    public String toString() {
        return "AppBrowserModel{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", landingUrl='" + landingUrl + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", viewOrder='" + viewOrder + '\'' +
                '}';
    }
}
