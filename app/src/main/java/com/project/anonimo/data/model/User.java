package com.project.anonimo.data.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userID")
    private String userID;
    @SerializedName("userName")
    private String userName;
    @SerializedName("userEmail")
    private String userEmail;
    @SerializedName("userPassword")
    private String userPassword;

    public User(String userID, String userName, String userEmail, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
