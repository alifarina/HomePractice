package com.poject.dalithub.models;


import java.util.ArrayList;
import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllBitesModel extends DalitHubBaseModel {

    @SerializedName("bites")
    @Expose
    private List<NewBite> bites = new ArrayList<NewBite>();
    @SerializedName("blockedBite")
    @Expose
    private List<Object> blockedBite = new ArrayList<Object>();

    @SerializedName("maxId")
    @Expose
    private String maxId;

    /**
     * @return The bites
     */
    public List<NewBite> getBites() {
        return bites;
    }

    /**
     * @param bites The bites
     */
    public void setBites(List<NewBite> bites) {
        this.bites = bites;
    }

    /**
     * @return The blockedBite
     */
    public List<Object> getBlockedBite() {
        return blockedBite;
    }

    /**
     * @param blockedBite The blockedBite
     */
    public void setBlockedBite(List<Object> blockedBite) {
        this.blockedBite = blockedBite;
    }


    /**
     * @return The maxId
     */
    public String getMaxId() {
        return maxId;
    }

    /**
     * @param maxId The maxId
     */
    public void setMaxId(String maxId) {
        this.maxId = maxId;
    }

}
