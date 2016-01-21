
package com.poject.dalithub.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MembersItems {

    @SerializedName("ImageUrl")
    @Expose
    private String ImageUrl;
    @SerializedName("HeaderName")
    @Expose
    private String HeaderName;
    @SerializedName("Distance")
    @Expose
    private String Distance;
    @SerializedName("members")
    @Expose
    private ArrayList<Member> members = new ArrayList<Member>();

    /**
     * @return The ImageUrl
     */
    public String getImageUrl() {
        return ImageUrl;
    }

    /**
     * @param ImageUrl The ImageUrl
     */
    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    /**
     * @return The HeaderName
     */
    public String getHeaderName() {
        return HeaderName;
    }

    /**
     * @param HeaderName The HeaderName
     */
    public void setHeaderName(String HeaderName) {
        this.HeaderName = HeaderName;
    }

    /**
     * @return The Distance
     */
    public String getDistance() {
        return Distance;
    }

    /**
     * @param Distance The Distance
     */
    public void setDistance(String Distance) {
        this.Distance = Distance;
    }

    /**
     * @return The members
     */
    public ArrayList<Member> getMembers() {
        return members;
    }

    /**
     * @param members The members
     */
    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }
}
