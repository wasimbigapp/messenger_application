package app.common.models;

/**
 * Created by bedprakash.r on 06/01/18.
 */

public interface TypeAwareModel {
    @ViewType
    int getType();
}
