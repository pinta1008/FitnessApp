package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class ReviewRequest {

    @SerializedName("username")
    String Username;
    @SerializedName("description")
    String  Description;

    public ReviewRequest() {
    }

    public ReviewRequest(String username, String description) {
        Username = username;
        Description = description;
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

}
