package com.example.fitnessapp.vjezbe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.models.ExerciseMaxWeight;
import com.example.fitnessapp.models.SingletonUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseMaxWeightActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseMaxWeightActivity";
    private RecyclerView recyclerView;
    private IFitnessApi fitnessApi;

    private ToggleButton toggleButton;
    private boolean isAllUsers = true;

    private ExerciseMaxWeightAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_max_weight);

        recyclerView = findViewById(R.id.recyclerView);
        toggleButton = findViewById(R.id.toggleButton);

        fitnessApi = RetrofitService.getClient().create(IFitnessApi.class);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAllUsers = isChecked;
                fetchExerciseMaxWeights();
            }
        });

        setupRecyclerView();

        // Fetch initial exercise max weight data based on the default selection
        fetchExerciseMaxWeights();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExerciseMaxWeightAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    private void fetchExerciseMaxWeights() {
        Call<List<ExerciseMaxWeight>> call;
        if (isAllUsers) {
            call = fitnessApi.getExerciseMaxWeights();
        } else {

            call = fitnessApi.getExerciseMaxWeights(SingletonUser.getInstance().getId());
        }

        call.enqueue(new Callback<List<ExerciseMaxWeight>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<List<ExerciseMaxWeight>> call, Response<List<ExerciseMaxWeight>> response) {
                if (response.isSuccessful()) {
                    List<ExerciseMaxWeight> exerciseMaxWeights = response.body();
                    adapter.updateData(exerciseMaxWeights);
                } else {
                    Log.e(TAG, "Failed to fetch exercise max weights");
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<List<ExerciseMaxWeight>> call, Throwable t) {
                Log.e(TAG, "Error fetching exercise max weights", t);
                Toast.makeText(ExerciseMaxWeightActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
