package com.project.anonimo.data.model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("commentID")
    private String commentID;
    @SerializedName("postID")
    private String postID;
    @SerializedName("commentContent")
    private String commentContent;
    @SerializedName("commentTime")
    private String commentTime;

    public Comment(String commentID, String postID, String commentContent, String commentTime) {
        this.commentID = commentID;
        this.postID = postID;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
}
