package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 12/29/2015.
 */
public class EventCommentModel implements Serializable {
    @SerializedName("eventcommentid")
    @Expose
    private String eventcommentid;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("posted_date")
    @Expose
    private String posted_date;
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
     * @return The eventcommentid
     */
    public String getEventcommentid() {
        return eventcommentid;
    }

    /**
     * @param eventcommentid The eventcommentid
     */
    public void setEventcommentid(String eventcommentid) {
        this.eventcommentid = eventcommentid;
    }

    /**
     * @return The eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * @param eventId The event_id
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
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
     * @return The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return The commentedTime
     */
    public String getCommentedTime() {
        return posted_date;
    }

    /**
     * @param commentedTime The commented_time
     */
    public void setCommentedTime(String commentedTime) {
        this.posted_date = commentedTime;
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
