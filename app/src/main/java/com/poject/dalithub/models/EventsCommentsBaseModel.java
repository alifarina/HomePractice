package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 1/6/2016.
 */
public class EventsCommentsBaseModel extends DalitHubBaseModel {

    @SerializedName("eventcomments")
    @Expose
    private List<EventCommentModel> eventcomments = new ArrayList<EventCommentModel>();


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


}
