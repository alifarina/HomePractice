package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 11/28/2015.
 */
public class DalitHubBaseModel {

    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("responseDescription")
    @Expose
    private String responseDescription;

    /**
     *
     * @return
     * The responseCode
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     *
     * @param responseCode
     * The responseCode
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     *
     * @return
     * The responseDescription
     */
    public String getResponseDescription() {
        return responseDescription;
    }

    /**
     *
     * @param responseDescription
     * The responseDescription
     */
    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

}
