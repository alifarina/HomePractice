package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2/2/2016.
 */
public class LikesBean {
    @SerializedName("biteid")
    @Expose
    private String biteid;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("user_location")
    @Expose
    private String userLocation;
    @SerializedName("posted_date")
    @Expose
    private String postedDate;

    /**
     * @return The biteid
     */
    public String getBiteid() {
        return biteid;
    }

    /**
     * @param biteid The biteid
     */
    public void setBiteid(String biteid) {
        this.biteid = biteid;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * @param profileImage The profile_image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * @return The userLocation
     */
    public String getUserLocation() {
        return userLocation;
    }

    /**
     * @param userLocation The user_location
     */
    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    /**
     * @return The postedDate
     */
    public String getPostedDate() {
        return postedDate;
    }

    /**
     * @param postedDate The posted_date
     */
    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }
}
