package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseMaxWeight {

    @SerializedName("userId")
    private String userId;

    @SerializedName("exerciseId")
    private int exerciseId;

    @SerializedName("exerciseName")
    private String exerciseName;

    @SerializedName("exercisePhoto")
    private String exercisePhoto;

    @SerializedName("maxWeight")
    private int maxWeight;

    public ExerciseMaxWeight(String userId, int exerciseId, String exerciseName, String exercisePhoto, int maxWeight) {
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.exercisePhoto = exercisePhoto;
        this.maxWeight = maxWeight;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExercisePhoto() {
        return exercisePhoto;
    }

    public void setExercisePhoto(String exercisePhoto) {
        this.exercisePhoto = exercisePhoto;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }
}
