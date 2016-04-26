package com.scenario2.routenavigation.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Class to check network availability, etc
 */
public class NetworkUtility {
    /**
     * Checks the network connection for the device
     * @return boolean
     */
    public static boolean haveNetworkConnection(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if ((activeNetwork != null)
                && ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                || (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)))
            return true;
        else
            return false;
    }
}
