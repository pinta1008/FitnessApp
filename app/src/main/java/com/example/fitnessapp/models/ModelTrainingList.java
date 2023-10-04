package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class ModelTrainingList {
    @SerializedName("id")
    int trainingId;

    @SerializedName("name")
    String trainingName;

    @SerializedName("photo")
    String trainingPhoto;
    @SerializedName("userId")
    String trainingBy;

    public ModelTrainingList() {
    }

    public ModelTrainingList(int trainingId, String trainingName, String trainingPhoto, String trainingBy) {
        this.trainingId = trainingId;
        this.trainingName = trainingName;
        this.trainingPhoto = trainingPhoto;
        this.trainingBy = trainingBy;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingPhoto() {
        return trainingPhoto;
    }

    public void setTrainingPhoto(String trainingPhoto) {
        this.trainingPhoto = trainingPhoto;
    }

    public String getTrainingBy() {
        return trainingBy;
    }

    public void setTrainingBy(String trainingBy) {
        this.trainingBy = trainingBy;
    }
}
