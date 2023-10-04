package com.example.fitnessapp.prijava;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.AllForUsers.DashboardUserActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.interfaces.LoginInterface;
import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.databinding.ActivityLoginBinding;
import com.example.fitnessapp.models.LoginRequest;
import com.example.fitnessapp.models.LoginResponse;
import com.example.fitnessapp.models.SingletonUser;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    private LoginInterface loginInterface;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Retrofit retrofit = RetrofitService.getClient();

        loginInterface = retrofit.create(LoginInterface.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Molimo pričekajte");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        binding.tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private String username = "", password = "";

    private void validateData() {

        username = binding.etLoginEmail.getText().toString().trim();
        password = binding.etLoginPass.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Ne može biti prazno", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ne može biti prazno", Toast.LENGTH_SHORT).show();
        } else {
            loginUser();
        }
    }

    private void loginUser() {

        progressDialog.setMessage("Prijava...");
        progressDialog.show();

            Call<LoginResponse> call = loginInterface.loginUser(new LoginRequest(username, password));
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {

                        LoginResponse loginResponse = response.body();
                        SingletonUser.getInstance().setSingletonUser(loginResponse.getUserId(), loginResponse.getJwtToken(), loginResponse.getUsername(), loginResponse.getEmail(), loginResponse.getPhoto(),
                                loginResponse.getRoleName(),loginResponse.getTreningID());
                        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
                        sharedEditor.putBoolean("provjera", true);
                        sharedEditor.putString("username", username);
                        sharedEditor.putString("password",password);
                        sharedEditor.apply();

                        checkUser(loginResponse.getRoleName());



                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Prijava neuspješna", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Prijava nije uspjela.", Toast.LENGTH_SHORT).show();
                }
            });


    }

    private void checkUser(String userType) {
        progressDialog.setMessage("Provjera korisnika...");
        if (userType.equals("User")) {
            startActivity(new Intent(LoginActivity.this, DashboardUserActivity.class));
            finish();
        } else if (userType.equals("Admin")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}
