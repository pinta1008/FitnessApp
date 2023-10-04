package com.example.fitnessapp.interfaces;

import com.example.fitnessapp.models.LoginRequest;
import com.example.fitnessapp.models.LoginResponse;
import com.example.fitnessapp.models.RegisterRequest;
import com.example.fitnessapp.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;


public interface LoginInterface {

    @POST("api/Authenticate/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("api/Authenticate/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);
}
