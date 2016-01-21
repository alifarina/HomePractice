package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 12/24/2015.
 */
public class EventAttendee {

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
}
