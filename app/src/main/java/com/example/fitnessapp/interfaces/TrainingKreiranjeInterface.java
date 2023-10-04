package com.example.fitnessapp.interfaces;

import android.view.Display;

import com.example.fitnessapp.models.ExerciseUser;
import com.example.fitnessapp.models.ExerciseUserRequest;
import com.example.fitnessapp.models.ModelTraining;
import com.example.fitnessapp.models.TreningRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrainingKreiranjeInterface {
    //sve za kreiranje treninga
    @POST("api/Trening/PostTrening")
    Call<ModelTraining> addToTraining(@Body TreningRequest trening);

    @DELETE("api/Trening/DeleteTrening/{id}")
    Call<Void> deleteTrening(@Path("id") int treningId);

    @GET("api/Trening/ListaTreninga/{userId}")
    Call<List<ModelTraining>> getTrainings(@Path("userId") String userId);

    @GET("api/Trening/GetTrening/{id}")
    Call<ModelTraining> getVjezbeUTreningu(@Path("id") int id);

    //dodavanje u trening fragment

    @GET("api/Trening/ListaUKosari/{userId}")
    Call<ModelTraining>getKosara(@Path("userId") String userId);

    @POST("api/Trening/PostInKosara")
    Call<ExerciseUser> addToTrainingKosara(@Body ExerciseUserRequest exerciseUserRequest);

    @DELETE("api/Trening/DeleteFromKosara/{exerciseUserId}")
    Call<Void> deleteFromTraining(@Path("exerciseUserId") int exerciseUserId);




}
