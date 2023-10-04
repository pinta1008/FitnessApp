package com.example.fitnessapp.Review;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import com.example.fitnessapp.R;

public class ReviewDetails extends AppCompatActivity {

    TextView detailIme, detailOpis;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        detailIme = findViewById(R.id.detailName);
        detailOpis = findViewById(R.id.detailOpis);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailIme.setText("Korisničko ime: " + bundle.getString("name"));
            detailOpis.setText("Opis: " + bundle.getString("desc"));

        }


    }
}