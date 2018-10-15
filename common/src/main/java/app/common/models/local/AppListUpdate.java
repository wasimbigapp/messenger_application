package app.common.models.local;

import android.support.annotation.NonNull;

import java.util.List;

import app.common.models.TypeAwareModel;
import app.common.models.ViewType;

/**
 * Model for bus event
 * Created by bedprakash.r on 09/01/18.
 */

public class AppListUpdate<T extends TypeAwareModel> {
    public final List<T> appCards;
    public int type;
    public int position;

    public AppListUpdate(@NonNull List<T> appCards, @ViewType int type, int position) {
        this.appCards = appCards;
        this.type = type;
        this.position = position;
    }

    public AppListUpdate(@NonNull List<T> appCards) {
        this.appCards = appCards;
        this.type = ViewType.EXTERNAL_APP;
    }
}
