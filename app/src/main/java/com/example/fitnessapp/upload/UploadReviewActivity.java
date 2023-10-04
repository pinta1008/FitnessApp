package com.example.fitnessapp.upload;

import android.content.Intent;
import android.os.Bundle;
import com.example.fitnessapp.AllForUsers.DashboardUserActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.databinding.ActivityUploadReviewBinding;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.models.ReviewRequest;
import com.example.fitnessapp.models.SingletonUser;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadReviewActivity extends AppCompatActivity {

    private ActivityUploadReviewBinding binding;
    Button saveButton;
    EditText uploadIme, uploadOpis;
    IFitnessApi iFitnessApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUploadReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        uploadIme = findViewById(R.id.uploadIme);
        uploadOpis = findViewById(R.id.uploadOpis);
        saveButton = findViewById(R.id.saveButton);

        uploadIme.setKeyListener(DigitsKeyListener.getInstance("Q W E R T Z U I O P Š Đ A S D F G H J K L Č Ć Ž Y X C V B N M q w e r t z u i o p š đ a s d" +
                "f g h j k l č ć ž y x c v b n m "));
        uploadIme.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        uploadOpis.setKeyListener(DigitsKeyListener.getInstance("Q W E R T Z U I O P Š Đ A S D F G H J K L Č Ć Ž Y X C V B N M q w e r t z u i o p š đ a s d" +
                "f g h j k l č ć ž y x c v b n m "));
        uploadOpis.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        Retrofit retrofit = RetrofitService.getClient();
        iFitnessApi = retrofit.create(IFitnessApi.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    public void validateData() {
        // Get data
        String ime = uploadIme.getText().toString().trim();
        String opis = uploadOpis.getText().toString().trim();

        // Validate data
        if (TextUtils.isEmpty(ime)) {
            Toast.makeText(UploadReviewActivity.this, "Upiši ime", Toast.LENGTH_SHORT).show();
        }
        else if (!ime.equals(SingletonUser.getInstance().getUserName()))
        {
            Toast.makeText(UploadReviewActivity.this, "Netočno korisničko ime", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(opis))
        {
            Toast.makeText(UploadReviewActivity.this, "Upiši problem ili prijedlog", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploadReview(ime, opis);
        }
    }

    private void uploadReview(String ime, String opis) {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setUsername(ime); // Set the username
        reviewRequest.setDescription(opis);

        Call<Void> call = iFitnessApi.addReview(reviewRequest); // Change the Call type to Void
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UploadReviewActivity.this, "Prijedlog ili problem spremljen.", Toast.LENGTH_SHORT).show();
                    // Redirect to Dashboard after successful upload
                    Intent intent = new Intent(UploadReviewActivity.this, DashboardUserActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UploadReviewActivity.this, "Prijedlog ili problem nisu spremljeni.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UploadReviewActivity.this, "Ne dela", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
