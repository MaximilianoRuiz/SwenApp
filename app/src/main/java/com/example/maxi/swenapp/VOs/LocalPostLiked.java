package com.example.maxi.swenapp.VOs;

/**
 * Created by Maxi on 14/12/2015.
 */
public class LocalPostLiked {

    private int id;
    private String postID;
    private boolean liked;

    public LocalPostLiked() {
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

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
