package com.poject.dalithub.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class CommentsModel extends DalitHubBaseModel {
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = new ArrayList<Comment>();

    @SerializedName("bitecount")
    @Expose
    private String bitecount;

    /**
     * @return The comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * @param comments The comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    /**
     * @return The bitecount
     */
    public String getBitecount() {
        return bitecount;
    }

    /**
     * @param bitecount The bitecount
     */
    public void setBitecount(String bitecount) {
        this.bitecount = bitecount;
    }
}