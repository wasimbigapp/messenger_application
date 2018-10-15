package app.common.models;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by bedprakash.r on 14/03/18.
 */
@IntDef({ErrorCodes.ERROR_UNKNOWN, ErrorCodes.EMPTY_NEWS})
@Retention(RetentionPolicy.SOURCE)
public @interface ErrorCodes {
    int ERROR_UNKNOWN = 1001;
    int EMPTY_NEWS = 1002;
}
