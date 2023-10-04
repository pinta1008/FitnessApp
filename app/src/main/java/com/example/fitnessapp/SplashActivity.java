package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.fitnessapp.AllForUsers.DashboardUserActivity;
import com.example.fitnessapp.interfaces.LoginInterface;
import com.example.fitnessapp.models.LoginRequest;
import com.example.fitnessapp.models.LoginResponse;
import com.example.fitnessapp.models.SingletonUser;
import com.example.fitnessapp.prijava.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {
    private LoginInterface loginInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Retrofit retrofit = RetrofitService.getClient();

        loginInterface = retrofit.create(LoginInterface.class);


        //start Login after 2 sec

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                loginUser();

            }
        }, 2000);
    }
    private void loginUser() {

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("provjera",false))
        {
            Call<LoginResponse> call = loginInterface.loginUser(new LoginRequest(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", "")));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        SingletonUser.getInstance().setSingletonUser(loginResponse.getUserId(), loginResponse.getJwtToken(), loginResponse.getUsername(), loginResponse.getEmail(), loginResponse.getPhoto(),
                                loginResponse.getRoleName(),loginResponse.getTreningID());

                        checkUser(loginResponse.getRoleName());

                    }
                    else
                    {
                        Toast.makeText(SplashActivity.this, "Login neuspješan.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    Toast.makeText(SplashActivity.this, "Login neje uspio. Pokušajte ponovno.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }


    }

    private void checkUser(String userType) {

        if (userType.equals("User")) {
            startActivity(new Intent(SplashActivity.this, DashboardUserActivity.class));
            finish();
        } else if (userType.equals("Admin")) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }
}