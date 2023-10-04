package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("userName")
    String userName;
    @SerializedName("password")
    String password;
    @SerializedName("email")
    String email;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
