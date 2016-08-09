package com.vstechlab.testapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activateNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activateNetworkInfo != null && activateNetworkInfo.isConnected();
    }
}
