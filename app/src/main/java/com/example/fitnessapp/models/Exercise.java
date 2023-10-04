package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class Exercise {

    @SerializedName("name")
    String Name;
    @SerializedName("photo")
    String Photo;
    @SerializedName("description")
    String Description;
    @SerializedName("info")
    String Info;

    @SerializedName("category")
    public Category category;
    @SerializedName("categoryId")
    int CatId;
    @SerializedName("id")
    int Id;
    @SerializedName("num_pon")
    int Num_pon;
    @SerializedName("num_ser")
    int Num_ser;
    @SerializedName("num_uk")
    int Num_uk;

    @SerializedName("weight")
    int Weight;

    public Exercise() {
    }

    public Exercise(String name, String photo, String description, String info, Category category, int catId, int id, int num_pon, int num_ser, int num_uk, int weight) {
        Name = name;
        Photo = photo;
        Description = description;
        Info = info;
        this.category = category;
        CatId = catId;
        Id = id;
        Num_pon = num_pon;
        Num_ser = num_ser;
        Num_uk = num_uk;
        Weight = weight;
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCatId() {
        return CatId;
    }

    public void setCatId(int catId) {
        CatId = catId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }
}
