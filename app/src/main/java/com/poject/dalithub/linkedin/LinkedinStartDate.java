package com.poject.dalithub.linkedin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedinStartDate {

    @SerializedName("month")
    @Expose
    public int month;
    @SerializedName("year")
    @Expose
    public int year;


    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


}