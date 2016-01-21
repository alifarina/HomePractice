package com.poject.dalithub.models;

/**
 * Created by admin on 11/28/2015.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel extends DalitHubBaseModel{

    @SerializedName("UserId")
    @Expose
    private String UserId;
    @SerializedName("IsAdmin")
    @Expose
    private String IsAdmin;

    /**
     *
     * @return
     * The UserId
     */
    public String getUserId() {
        return UserId;
    }

    /**
     *
     * @param UserId
     * The UserId
     */
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    /**
     *
     * @return
     * The IsAdmin
     */
    public String getIsAdmin() {
        return IsAdmin;
    }

    /**
     *
     * @param IsAdmin
     * The IsAdmin
     */
    public void setIsAdmin(String IsAdmin) {
        this.IsAdmin = IsAdmin;
    }

}
