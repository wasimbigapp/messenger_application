package bpr10.git.crhometabs.chrometabs;

import android.support.customtabs.CustomTabsClient;

/**
 * Created by bedprakash.r on 17/01/18.
 */

interface ServiceConnectionCallback {
    /**
     * Called when the service is connected.
     *
     * @param client a CustomTabsClient
     */
    void onServiceConnected(CustomTabsClient client);

    /**
     * Called when the service is disconnected.
     */
    void onServiceDisconnected();
}
