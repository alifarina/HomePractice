package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/18/2015.
 */
public class EventBaseClass extends DalitHubBaseModel {

    @SerializedName("events")
    @Expose
    private List<EventModel> events = new ArrayList<EventModel>();

    /**
     * @return The events
     */
    public List<EventModel> getEvents() {
        return events;
    }

    /**
     * @param events The events
     */
    public void setEvents(List<EventModel> events) {
        this.events = events;
    }


}
