package messenger.messenger.messanger.messenger.model;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;

/**
 * App launch card
 * Created by bedprakash.r on 14/01/18.
 */

public class AppLaunchCountModel implements TypeAwareModel {

    private int launchCount;
    private String mLabel;
    private String packageName;

    public AppLaunchCountModel() {
    }

    public String getLabel() {
        return mLabel;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    @Override
    public String toString() {
        return mLabel;
    }

    @ViewType
    @Override
    public int getType() {
        return ViewType.APP_LAUNCH_CARD;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }
}
