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

    public void setEmailStatus(boolean status) {
        pref.edit().putBoolean("showEmail", status).commit();
    }

    public void setMobileStatus(boolean status) {
        pref.edit().putBoolean("showMobile", status).commit();
    }

    public void setCompStatus(boolean status) {
        pref.edit().putBoolean("showComp", status).commit();
    }

    public void setOtherStatus(boolean status) {
        pref.edit().putBoolean("showOther", status).commit();
    }

    public boolean showEmailStatus() {
        return pref.getBoolean("showEmail", false);
    }

    public boolean showMobileStatus() {
        return pref.getBoolean("showMobile", false);
    }

    public boolean showCompStatus() {
        return pref.getBoolean("showComp", false);
    }

    public boolean showOtherStatus() {
        return pref.getBoolean("showOther", false);
    }

    public void setAccountVerified(boolean status) {
        pref.edit().putBoolean("account_verify", status).commit();
    }

    public boolean isAccountVerified() {
        return pref.getBoolean("account_verify", false);
    }
}
