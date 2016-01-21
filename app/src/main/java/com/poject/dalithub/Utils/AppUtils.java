package com.poject.dalithub.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.widget.Toast;

public class AppUtils {
    public static int DEVICE_HEIGHT, DEVICE_WIDTH;

    public static void updateDeviceResolution(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        DEVICE_HEIGHT = displaymetrics.heightPixels;
        DEVICE_WIDTH = displaymetrics.widthPixels;
    }

    public static boolean isInternetAvailable(Context con) {

        ConnectivityManager cManager = (ConnectivityManager) con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable() && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmailAddress(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidUrl(String link) {
        return Patterns.WEB_URL.matcher(link).matches();
    }

    public static void showToast(Context context, String text) {

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
