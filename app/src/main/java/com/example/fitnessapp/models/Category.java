package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("name")
    String Name;

    @SerializedName("photo")
    String Photo;

    @SerializedName("id")
    int Id;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Category(String name, String photo, int id) {
        Name = name;
        Photo = photo;
        Id = id;
    }

    public Category(String name, String photo) {
        Name = name;
        Photo = photo;
    }

    public Category() {
    }
}
