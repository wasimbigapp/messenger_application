package app.common.models;

import java.io.Serializable;

import bpr10.git.common.R;

import static app.common.utils.Utils.getString;

/**
 * Created by bedprakash.r on 14/03/18.
 */

public class ErrorInfo implements Serializable {
    public String errorMessage;
    public int errorCode;

    public ErrorInfo() {
        this.errorMessage = getString(R.string.error_generic);
        this.errorCode = ErrorCodes.ERROR_UNKNOWN;
    }


    public ErrorInfo(String errorMessage, @ErrorCodes int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
