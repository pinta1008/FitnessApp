package com.example.fitnessapp.trening;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;

import android.widget.TextView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.interfaces.TrainingKreiranjeInterface;
import com.example.fitnessapp.models.ExerciseUser;
import com.example.fitnessapp.models.ModelTraining;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingListDetails extends AppCompatActivity {
    private String trainingBy, trainingName;
    private int trainingId;

    // UI views
    private SearchView searchView;
    private TextView broj_vjezbi;
    private RecyclerView recyclerView;

    private ArrayList<ExerciseUser> modelCreatedTrainingArrayList;
    private TrainingExerciseAdapter adapter;

    private TrainingKreiranjeInterface trainingKreiranjeInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_list_details);

        searchView = findViewById(R.id.sBar);
        searchView.clearFocus();
        broj_vjezbi = findViewById(R.id.broj_vjezbi);
        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(TrainingListDetails.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Values received through intent
        Bundle bundle = getIntent().getExtras();
        trainingBy = bundle.getString("userId");
        trainingId = bundle.getInt("id");
        trainingName = bundle.getString("name");

        loadTrainingInfo();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchList(s);
                return true;
            }
        });
    }

    private void loadTrainingInfo() {
        trainingKreiranjeInterface = RetrofitService.getClient().create(TrainingKreiranjeInterface.class);
        Call<ModelTraining> call = trainingKreiranjeInterface.getVjezbeUTreningu(trainingId);
        call.enqueue(new Callback<ModelTraining>() {
            @Override
            public void onResponse(Call<ModelTraining> call, Response<ModelTraining>response) {
                if (response.isSuccessful()) {
                   ModelTraining training  = response.body();
                    if (training != null) {
                        modelCreatedTrainingArrayList = new ArrayList<>(training.getVjezbe());
                        adapter = new TrainingExerciseAdapter(TrainingListDetails.this, modelCreatedTrainingArrayList, trainingId);
                        recyclerView.setAdapter(adapter);
                        broj_vjezbi.setText(String.valueOf(modelCreatedTrainingArrayList.size()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelTraining> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void searchList(String text) {
        ArrayList<ExerciseUser> searchList = new ArrayList<>();
        for (ExerciseUser exerciseUser : modelCreatedTrainingArrayList) {
            if (exerciseUser.getExercise().getName().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(exerciseUser);
            }
        }
        adapter.searchDataList(searchList);
    }
}
