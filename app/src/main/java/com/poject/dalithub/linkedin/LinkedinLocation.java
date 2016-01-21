package com.poject.dalithub.linkedin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedinLocation {

    @SerializedName("country")
    @Expose
    public LinkedinCountry country;
    @SerializedName("name")
    @Expose
    public String name;

    public LinkedinCountry getCountry() {
        return country;
    }

    public void setCountry(LinkedinCountry country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}