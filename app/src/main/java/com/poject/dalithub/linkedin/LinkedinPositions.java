package com.poject.dalithub.linkedin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LinkedinPositions {

    @SerializedName("_total")
    @Expose
    public int Total;
    @SerializedName("values")
    @Expose
    public List<LinkedinValue> values = new ArrayList<LinkedinValue>();

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<LinkedinValue> getValues() {
        return values;
    }

    public void setValues(List<LinkedinValue> values) {
        this.values = values;
    }



}