package com.poject.dalithub.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    Activity mActivity;
    SharedPreferences pref;

    public UserPreferences(Activity act) {
        mActivity = act;
        pref = mActivity.getSharedPreferences("myPref", Context.MODE_PRIVATE);
    }

    public void saveUserId(String id) {
        pref.edit().putString("userId", id).commit();
    }

    public String getUserId() {
        return pref.getString("userId", "");
    }

    public void setIsAdmin(String admin) {
        pref.edit().putString("isAdmin", admin).commit();
    }

    public String isAdmin() {
        return pref.getString("isAdmin", "");
    }
}
