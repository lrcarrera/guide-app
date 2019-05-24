package com.example.pathfinderapp.AsyncStuff;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.pathfinderapp.R;

public class NetworkUtil {

    public static String getConnectivityStatusString(Context context) {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = context.getResources().getString(R.string.wifi_ok);
                return status;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = context.getResources().getString(R.string.mobile_ok);
                return status;
            }
        } else {
            status = context.getResources().getString(R.string.internet_ko);
            return status;
        }
        return null;
    }
}