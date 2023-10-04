package com.example.fitnessapp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

        private static final String BASE_URL =  "http://10.0.2.2:5058/"; //"https://fitnessapitest.azurewebsites.net/"   // Replace with your API base URL

        private static Retrofit retrofit;

        public static Retrofit getClient() {
            if (retrofit == null) {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.MINUTES)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL).client(okHttpClient).
                        addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

