package com.certis.login;

public class User {
    private String userID;
    private String userPW;

    public String getUserID() {
        return userID;
    }

    public User setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getUserPW() {
        return userPW;
    }

    public User setUserPW(String userPW) {
        this.userPW = userPW;
        return this;
    }
}
