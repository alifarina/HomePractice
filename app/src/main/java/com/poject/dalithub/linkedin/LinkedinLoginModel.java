package com.poject.dalithub.linkedin;

/**
 * Created by awdesh.kumar on 25-Sep-15.
 */

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedinLoginModel {

    @SerializedName("emailAddress")
    @Expose
    public String emailAddress;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("headline")
    @Expose
    public String headline;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("industry")
    @Expose
    public String industry;
    @SerializedName("lastName")
    @Expose
    public String lastName;
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("pictureUrl")
    @Expose
    public String pictureUrl;
    @SerializedName("positions")
    @Expose
    public LinkedinPositions positions;
    @SerializedName("publicProfileUrl")
    @Expose
    public String publicProfileUrl;
    @SerializedName("summary")
    @Expose
    public String summary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public LinkedinPositions getPositions() {
        return positions;
    }

    public void setPositions(LinkedinPositions positions) {
        this.positions = positions;
    }

    public String getPublicProfileUrl() {
        return publicProfileUrl;
    }

    public void setPublicProfileUrl(String publicProfileUrl) {
        this.publicProfileUrl = publicProfileUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }



}