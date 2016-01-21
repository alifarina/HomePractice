package com.poject.dalithub.linkedin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedinCountry {

    @SerializedName("code")
    @Expose
    public String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



}