package com.example.fitnessapp.Review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.interfaces.TrainingKreiranjeInterface;
import com.example.fitnessapp.models.ModelTraining;
import com.example.fitnessapp.models.Review;
import com.example.fitnessapp.models.SingletonUser;
import com.example.fitnessapp.trening.TrainingListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Review> reviewsList;

    ReviewAdapter adapter;

    private IFitnessApi fitnessApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        recyclerView = findViewById(R.id.recyclerViewReview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ReviewActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        reviewsList = new ArrayList<>();

        fitnessApi = RetrofitService.getClient().create(IFitnessApi.class);

        adapter = new ReviewAdapter(this, reviewsList, fitnessApi);
        recyclerView.setAdapter(adapter);



        loadReview();
    }
    private void loadReview() {
        Call<List<Review>> call = fitnessApi.getReview();
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(@NonNull Call<List<Review>> call, @NonNull Response<List<Review>> response) {
                if (response.isSuccessful()) {
                    List<Review> reviewList = response.body();
                    if (reviewList != null) {
                        reviewsList.clear();
                        reviewsList.addAll(reviewList);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(ReviewActivity.this, "Neuspješno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Review>> call, @NonNull Throwable t) {
                Toast.makeText(ReviewActivity.this, "Neuspješno očitavanje: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}