package com.project.anonimo.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    @SerializedName("postID")
    private String postID;
    @SerializedName("userID")
    private String userID;
    @SerializedName("postContent")
    private String postContent;
    @SerializedName("postTag")
    private String postTag;
    @SerializedName("postTime")
    private String postTime;
    @SerializedName("comments")
    private List<Comment> comments;

    public Post(String postID, String userID, String postContent, String postTag, String postTime, List<Comment> comments) {
        this.postID = postID;
        this.userID = userID;
        this.postContent = postContent;
        this.postTag = postTag;
        this.postTime = postTime;
        this.comments = comments;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostTag() {
        return postTag;
    }

    public void setPostTag(String postTag) {
        this.postTag = postTag;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
