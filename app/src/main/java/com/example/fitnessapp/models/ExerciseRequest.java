package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseRequest {

    @SerializedName("name")
     String Name;
    @SerializedName("description")
     String Description;

    @SerializedName("info")
     String Info;

    @SerializedName("photo")
     String Photo;
    @SerializedName("num_pon")
     int Num_pon;
    @SerializedName("num_ser")
     int Num_ser;
    @SerializedName("num_uk")
    int Num_uk;
    @SerializedName("categoryId")
     int CategoryId;
    @SerializedName("weight")
    int Weight;

    public ExerciseRequest(String name, String description, String info, String photo, int num_pon, int num_ser, int num_uk, int categoryId, int weight) {
        Name = name;
        Description = description;
        Info = info;
        Photo = photo;
        Num_pon = num_pon;
        Num_ser = num_ser;
        Num_uk = num_uk;
        CategoryId = categoryId;
        Weight = weight;
    }

    public ExerciseRequest(String name, String description, String info, String photo, int categoryId, int photoPicker) {
        Name = name;
        Description = description;
        Info = info;
        Photo = photo;
        CategoryId = categoryId;
        Num_pon = photoPicker;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public int getNum_pon() {
        return Num_pon;
    }

    public void setNum_pon(int num_pon) {
        Num_pon = num_pon;
    }

    public int getNum_ser() {
        return Num_ser;
    }

    public void setNum_ser(int num_ser) {
        Num_ser = num_ser;
    }

    public int getNum_uk() {
        return Num_uk;
    }

    public void setNum_uk(int num_uk) {
        Num_uk = num_uk;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }
}
