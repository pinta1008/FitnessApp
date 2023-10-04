package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class TreningRequest {


    @SerializedName("id")
    Integer trainingId;

    @SerializedName("name")

    String Name;

    @SerializedName("photo")
    String Photo;

    public TreningRequest(Integer trainingId, String name, String photo) {
        this.trainingId = trainingId;
        Name = name;
        Photo = photo;
    }


}
