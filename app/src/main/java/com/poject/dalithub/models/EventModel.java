package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 12/18/2015.
 */
public class EventModel {

    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("event_time")
    @Expose
    private String eventTime;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("year")
    @Expose
    private String year;

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
     * @return The dateofmonth
     */
//    public String getDateofmonth() {
//        return dateofmonth;
//    }

    /**
     * @param dateofmonth The dateofmonth
     */
//    public void setDateofmonth(String dateofmonth) {
//        this.dateofmonth = dateofmonth;
//    }

    /**
     * @return The dateofweek
     */
//    public String getDateofweek() {
//        return dateofweek;
//    }

    /**
     * @param dateofweek The dateofweek
     */
//    public void setDateofweek(String dateofweek) {
//        this.dateofweek = dateofweek;
//    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
