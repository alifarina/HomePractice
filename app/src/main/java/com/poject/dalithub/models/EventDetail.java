package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 12/24/2015.
 */
public class EventDetail {

    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("event_description")
    @Expose
    private String eventDescription;
    @SerializedName("event_address")
    @Expose
    private String eventAddress;
    @SerializedName("latitide")
    @Expose
    private String latitide;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("web_link")
    @Expose
    private String webLink;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("event_time")
    @Expose
    private String eventTime;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("description")
    @Expose
    private String description;

    /**
     * @return The eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName The event_name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * @return The eventDescription
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * @param eventDescription The event_description
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * @return The eventAddress
     */
    public String getEventAddress() {
        return eventAddress;
    }

    /**
     * @param eventAddress The event_address
     */
    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    /**
     * @return The latitide
     */
    public String getLatitide() {
        return latitide;
    }

    /**
     * @param latitide The latitide
     */
    public void setLatitide(String latitide) {
        this.latitide = latitide;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The webLink
     */
    public String getWebLink() {
        return webLink;
    }

    /**
     * @param webLink The web_link
     */
    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    /**
     * @return The imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath The image_path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return The eventDate
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * @param eventDate The event_date
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * @return The eventTime
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime The event_time
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * @return The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * @param rating The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
