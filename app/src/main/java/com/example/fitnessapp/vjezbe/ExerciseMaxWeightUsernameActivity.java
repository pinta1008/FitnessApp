package com.example.fitnessapp.vjezbe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ToggleButton;

import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.models.ExerciseMaxWeightWithUsername;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseMaxWeightUsernameActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseMaxWeightUsernameActivity";
    private RecyclerView recyclerView;
    private IFitnessApi fitnessApi;

    private ExerciseMaxWeightUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_max_weight_username);

        recyclerView = findViewById(R.id.recyclerView);
        fitnessApi = RetrofitService.getClient().create(IFitnessApi.class);

        setupRecyclerView();

        // Fetch initial exercise max weight data based on the default selection
        fetchExerciseMaxWeights();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExerciseMaxWeightUserAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }
    private void fetchExerciseMaxWeights() {

        Call<List<ExerciseMaxWeightWithUsername>> call = fitnessApi.getExerciseMaxWeightsWithUsername();
        call.enqueue(new Callback<List<ExerciseMaxWeightWithUsername>>() {
            @Override
            public void onResponse(Call<List<ExerciseMaxWeightWithUsername>> call, Response<List<ExerciseMaxWeightWithUsername>> response) {
                if (response.isSuccessful()) {
                    List<ExerciseMaxWeightWithUsername> data = response.body();
                    // Update your adapter with the data
                    adapter.updateData(data);
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseMaxWeightWithUsername>> call, Throwable t) {
                // Handle network failure
            }
        });
    }
}