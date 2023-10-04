package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("id")
     int Id;
    @SerializedName("username")
     String Username;
    @SerializedName("description")
     String  Description;
    @SerializedName("userId")
    String userId;

    public Review(int id, String username, String description, String userId) {
        Id = id;
        Username = username;
        Description = description;
        this.userId = userId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
