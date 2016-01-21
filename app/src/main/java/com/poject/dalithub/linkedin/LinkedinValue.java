package com.poject.dalithub.linkedin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedinValue {

    @SerializedName("company")
    @Expose
    public LinkedinCompany company;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("isCurrent")
    @Expose
    public boolean isCurrent;
    @SerializedName("startDate")
    @Expose
    public LinkedinStartDate startDate;
    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("title")
    @Expose
    public String title;

    public LinkedinStartDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LinkedinStartDate startDate) {
        this.startDate = startDate;
    }

    public LinkedinCompany getCompany() {
        return company;
    }

    public void setCompany(LinkedinCompany company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}