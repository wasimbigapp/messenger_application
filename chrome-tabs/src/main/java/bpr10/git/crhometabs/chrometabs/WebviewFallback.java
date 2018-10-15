package bpr10.git.crhometabs.chrometabs;

import android.content.Context;
import android.net.Uri;

/**
 * Created by bedprakash.r on 17/01/18.
 */

public class WebviewFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Context context, Uri uri) {
        ChromeUtils.openInInetrnalBrowser(context, uri.toString());
    }
}