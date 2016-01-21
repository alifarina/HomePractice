package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewBite {


    @SerializedName("biteid")
    @Expose
    private String biteid;
    @SerializedName("bite_message")
    @Expose
    private String biteMessage;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("commentcount")
    @Expose
    private String commentcount;
    @SerializedName("likecount")
    @Expose
    private String likecount;
    @SerializedName("likestatus")
    @Expose
    private String likestatus;
    @SerializedName("posted_user_id")
    @Expose
    private String postedUserId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("user_location")
    @Expose
    private String userLocation;

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
     * @return The biteMessage
     */
    public String getBiteMessage() {
        return biteMessage;
    }

    /**
     * @param biteMessage The bite_message
     */
    public void setBiteMessage(String biteMessage) {
        this.biteMessage = biteMessage;
    }

    /**
     * @return The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
     * @return The commentcount
     */
    public String getCommentcount() {
        return commentcount;
    }

    /**
     * @param commentcount The commentcount
     */
    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    /**
     * @return The likecount
     */
    public String getLikecount() {
        return likecount;
    }

    /**
     * @param likecount The likecount
     */
    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    /**
     * @return The likestatus
     */
    public String getLikestatus() {
        return likestatus;
    }

    /**
     * @param likestatus The likestatus
     */
    public void setLikestatus(String likestatus) {
        this.likestatus = likestatus;
    }

    /**
     * @return The postedUserId
     */
    public String getPostedUserId() {
        return postedUserId;
    }

    /**
     * @param postedUserId The posted_user_id
     */
    public void setPostedUserId(String postedUserId) {
        this.postedUserId = postedUserId;
    }

    /**
     * @return The time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(String time) {
        this.time = time;
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