package com.poject.dalithub.models;

import com.google.code.linkedinapi.schema.Likes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2/2/2016.
 */
public class UserLikes extends DalitHubBaseModel {
    @SerializedName("comments")
    @Expose
    private List<LikesBean> comments = new ArrayList<LikesBean>();

    @SerializedName("bitecount")
    @Expose
    private String bitecount;

    /**
     * @return The comments
     */
    public List<LikesBean> getComments() {
        return comments;
    }

    /**
     * @param comments The comments
     */
    public void setComments(List<LikesBean> comments) {
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
