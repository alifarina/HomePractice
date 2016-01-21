package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 11/28/2015.
 */
public class SignUpModel extends DalitHubBaseModel{

    @SerializedName("UserId")
    @Expose
    private String UserId;


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
}
