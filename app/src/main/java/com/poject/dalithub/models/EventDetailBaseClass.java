package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/24/2015.
 */
public class EventDetailBaseClass extends DalitHubBaseModel {


    @SerializedName("eventDetails")
    @Expose
    private List<EventDetail> eventDetails = new ArrayList<EventDetail>();
    @SerializedName("eventcomments")
    @Expose
    private List<EventCommentModel> eventcomments = new ArrayList<EventCommentModel>();
    @SerializedName("eventAttendees")
    @Expose
    private List<EventAttendee> eventAttendees = new ArrayList<EventAttendee>();


    /**
     * @return The eventDetails
     */
    public List<EventDetail> getEventDetails() {
        return eventDetails;
    }

    /**
     * @param eventDetails The eventDetails
     */
    public void setEventDetails(List<EventDetail> eventDetails) {
        this.eventDetails = eventDetails;
    }

    /**
     * @return The eventcomments
     */
    public List<EventCommentModel> getEventcomments() {
        return eventcomments;
    }

    /**
     * @param eventcomments The eventcomments
     */
    public void setEventcomments(List<EventCommentModel> eventcomments) {
        this.eventcomments = eventcomments;
    }

    /**
     * @return The eventAttendees
     */
    public List<EventAttendee> getEventAttendees() {
        return eventAttendees;
    }

    /**
     * @param eventAttendees The eventAttendees
     */
    public void setEventAttendees(List<EventAttendee> eventAttendees) {
        this.eventAttendees = eventAttendees;
    }


}
