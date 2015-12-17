package com.example.maxi.swenapp.VOs;

/**
 * Created by Maxi on 14/12/2015.
 */
public class LocalPostComment {

    private int id;
    private String postID;
    private String comment;

    public LocalPostComment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
