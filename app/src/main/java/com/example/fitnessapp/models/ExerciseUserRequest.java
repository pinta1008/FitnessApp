package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseUserRequest {

    @SerializedName("userId")
    private String id;

    @SerializedName("num_pon")
    private int num_pon;

    @SerializedName("num_ser")
    private int num_ser;
    @SerializedName("num_uk")
    private int num_uk;

    @SerializedName("exerciseId")
    private int exerciseId;
    @SerializedName("treningId")
    private Integer treningId;

    @SerializedName("weight")
    private int weight;

    public ExerciseUserRequest() {

    }

    // Constructor
    public ExerciseUserRequest(String id, int num_pon, int num_ser, int num_uk, int exerciseId, Integer treningId, int weight) {
        this.id = id;
        this.num_pon = num_pon;
        this.num_ser = num_ser;
        this.num_uk = num_uk;
        this.exerciseId = exerciseId;
        this.treningId = treningId;
        this.weight = weight;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum_pon() {
        return num_pon;
    }

    public void setNum_pon(int num_pon) {
        this.num_pon = num_pon;
    }

    public int getNum_ser() {
        return num_ser;
    }

    public void setNum_ser(int num_ser) {
        this.num_ser = num_ser;
    }

    public int getNum_uk() {
        return num_uk;
    }

    public void setNum_uk(int num_uk) {
        this.num_uk = num_uk;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getTreningId() {
        return treningId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

