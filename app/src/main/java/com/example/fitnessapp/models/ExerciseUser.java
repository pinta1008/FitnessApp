package com.example.fitnessapp.models;

import com.google.gson.annotations.SerializedName;

public class ExerciseUser {
    @SerializedName("id")
     int Id;

    @SerializedName("num_pon")
     int Num_pon;

    @SerializedName("num_ser")
     int Num_ser;

    @SerializedName("num_uk")
     int Num_uk;

    @SerializedName("exercise")
    public Exercise exercise;

    @SerializedName("exerciseId")
     int ExerciseId;

    @SerializedName("treningId")
     int TreningId;

    @SerializedName("weight")
    int Weight;




    public ExerciseUser(int id, int num_pon, int num_ser, int num_uk, Exercise exercise, int exerciseId, int treningId, int weight) {
        Id = id;
        Num_pon = num_pon;
        Num_ser = num_ser;
        Num_uk = num_uk;
        this.exercise = exercise;
        ExerciseId = exerciseId;
        TreningId = treningId;
        Weight = weight;
    }



    public ExerciseUser() {

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

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getExerciseId() {
        return ExerciseId;
    }

    public void setExerciseId(int exerciseId) {
        ExerciseId = exerciseId;
    }

    public int getTreningId() {
        return TreningId;
    }

    public void setTreningId(int treningId) {
        TreningId = treningId;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }
}
