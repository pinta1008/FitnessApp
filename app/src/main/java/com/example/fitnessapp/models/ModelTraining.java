package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

public class ModelTraining {

    @SerializedName("id")
        Integer id;
    @SerializedName("userId")
     String userId;
    @SerializedName("trainingId")
    Integer trainingId;
    @SerializedName("name")

    String Name;
    @SerializedName("createdDate")
     String createdDate;

    @SerializedName("photo")
     String Photo;

    @SerializedName("flag")

     int Flag;
    @SerializedName("vjezbe")
    public List<ExerciseUser> vjezbe;




    public ModelTraining(Integer id, String userId, Integer trainingId, String name, String photo, int flag, List<ExerciseUser> vjezbe, String createdDate) {
        this.id = id;
        this.userId = userId;
        this.trainingId = trainingId;
        Name = name;
        Photo = photo;
        Flag = flag;
        this.vjezbe = vjezbe;
        this.createdDate = createdDate;
    }

    public ModelTraining() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
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

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }

    public List<ExerciseUser> getVjezbe() {
        return vjezbe;
    }

    public void setVjezbe(List<ExerciseUser> vjezbe) {
        this.vjezbe = vjezbe;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
