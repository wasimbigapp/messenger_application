package app.common.models;

/**
 * Created by bedprakash.r on 06/01/18.
 */

public class TypeAwareModelImpl implements TypeAwareModel {
    @ViewType
    private final int type;

    public TypeAwareModelImpl(@ViewType int type) {
        this.type = type;
    }

    @ViewType
    public int getType() {
        return type;
    }
}
