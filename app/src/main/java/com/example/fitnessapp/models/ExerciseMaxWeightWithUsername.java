package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseMaxWeightWithUsername {
    @SerializedName("exerciseId")
    private int exerciseId;
    @SerializedName("exerciseName")
    private String exerciseName;
    @SerializedName("exercisePhoto")
    private String exercisePhoto;
    @SerializedName("maxWeight")
    private int maxWeight;
    @SerializedName("userId")
    private String userId;
    @SerializedName("username")
    private String username;

    public ExerciseMaxWeightWithUsername(int exerciseId, String exerciseName, String exercisePhoto, int maxWeight, String userId, String username) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.exercisePhoto = exercisePhoto;
        this.maxWeight = maxWeight;
        this.userId = userId;
        this.username = username;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
