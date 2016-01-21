
package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MembersBaseClass extends DalitHubBaseModel {


    @SerializedName("items")
    @Expose
    private ArrayList<MembersItems> items = new ArrayList<MembersItems>();
    @SerializedName("MembersCount")
    @Expose
    private String MembersCount;


    /**
     * @return The items
     */
    public ArrayList<MembersItems> getItems() {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(ArrayList<MembersItems> items) {
        this.items = items;
    }

    /**
     * @return The MembersCount
     */
    public String getMembersCount() {
        return MembersCount;
    }

    /**
     * @param MembersCount The MembersCount
     */
    public void setMembersCount(String MembersCount) {
        this.MembersCount = MembersCount;
    }


}
