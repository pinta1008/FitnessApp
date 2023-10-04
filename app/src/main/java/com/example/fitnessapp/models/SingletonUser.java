package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class SingletonUser {

    private static SingletonUser singletonUser;

    String id;
    String jwt;
    String userName;
    String email;

    String photo;

    String role;

    Integer treningId;

    public static SingletonUser getInstance()
    {
        if(singletonUser == null)
        {
            synchronized (SingletonUser.class)
            {
                if(singletonUser == null)
                {
                    singletonUser = new SingletonUser();
                }
            }
        }
        return singletonUser;
    }

    public  void setSingletonUser(String id, String jwt, String userName, String email, String photo, String role, Integer treningId) {
        singletonUser.id = id;
        singletonUser.jwt = "Bearer " + jwt;
        singletonUser.userName = userName;
        singletonUser.email = email;
        singletonUser.photo = photo;
        singletonUser.role = role;
        singletonUser.treningId = treningId;
    }


    public String getId() {
        return id;
    }

    public String getJwt() {
        return jwt;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getTreningId() {
        return treningId;
    }

    public void setTreningId(Integer treningId) {
        this.treningId = treningId;
    }

    public String getRole() {
        return role;
    }
}
