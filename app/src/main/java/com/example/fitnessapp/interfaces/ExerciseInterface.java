package com.example.fitnessapp.interfaces;
import com.example.fitnessapp.models.Exercise;
import com.example.fitnessapp.models.ExerciseRequest;
import com.example.fitnessapp.models.ExerciseUser;
import com.example.fitnessapp.models.ExerciseUserRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ExerciseInterface {

        @GET("api/Exercise/getAll")
        Call<List<Exercise>> getExercises(@Header("Authentication")String Jwt);

        @GET("api/Exercise/GetExercise/{id}")
        Call<Exercise> getOneExercise(@Header("Authentication")String Jwt, @Path("id") int id);

        @POST("api/Exercise/Create")
        Call<Exercise> addExercise(@Body ExerciseRequest exerciseRequest);

        @DELETE("api/Exercise/DeleteExercise/{id}")
        Call<Void> deleteExercise(@Path("id") int id);

        @PUT("api/Exercise/Update/{id}")
        Call<Void> updateExercise(@Path("id") int id, @Body ExerciseRequest exerciseRequest);
}
