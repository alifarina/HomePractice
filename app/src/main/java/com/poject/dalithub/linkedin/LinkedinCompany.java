package com.poject.dalithub.linkedin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedinCompany {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("industry")
    @Expose
    public String industry;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("size")
    @Expose
    public String size;
    @SerializedName("type")
    @Expose
    public String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}