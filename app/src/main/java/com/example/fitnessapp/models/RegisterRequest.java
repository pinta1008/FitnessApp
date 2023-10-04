package com.example.fitnessapp.models;

public class RegisterRequest {

    String username;
    String password;
    String email;

    String photo;


    public RegisterRequest(String username, String password,String email, String photo) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.photo = photo;
    }
}
