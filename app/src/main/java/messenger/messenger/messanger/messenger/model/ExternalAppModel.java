package messenger.messenger.messanger.messenger.model;

import com.google.gson.annotations.SerializedName;

import app.common.models.ViewType;

/**
 * Created by bedprakash.r on 14/03/18.
 */

public class ExternalAppModel extends AppBrowserModel {

    @SerializedName("deep_link")
    public String deeplink;

    private int launchCount;

    @ViewType
    @Override
    public int getType() {
        return ViewType.EXTERNAL_APP;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }
}
