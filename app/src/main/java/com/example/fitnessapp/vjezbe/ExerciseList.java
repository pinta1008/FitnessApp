package com.example.fitnessapp.vjezbe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.interfaces.ExerciseInterface;
import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.models.Exercise;
import com.example.fitnessapp.models.SingletonUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Exercise> dataList;
    private ExerciseAdapter adapter;
    private ExerciseInterface exerciseInterface;
    private androidx.appcompat.widget.SearchView searchView;
    private Button btnDelete;
    private Integer categoryId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.SearchBar);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ExerciseList.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new ExerciseAdapter(ExerciseList.this, dataList);
        recyclerView.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            categoryId = bundle.getInt("CategoryId");
        }

        exerciseInterface = RetrofitService.getClient().create(ExerciseInterface.class);

        loadData();

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

    private void loadData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseList.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        Call<List<Exercise>> call = exerciseInterface.getExercises(SingletonUser.getInstance().getJwt());
        call.enqueue(new Callback<List<Exercise>>() {
            @Override
            public void onResponse(@NonNull Call<List<Exercise>> call, @NonNull Response<List<Exercise>> response) {
                if (response.isSuccessful()) {
                    dataList.clear();
                    List<Exercise> exerciseList = response.body();
                    if (exerciseList != null) {
                        for (Exercise exercise : exerciseList) {
                            if (exercise.getCatId() == categoryId) {
                                dataList.add(exercise);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ExerciseList.this, "Neuspješno očitavanje vježbe", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<List<Exercise>> call, @NonNull Throwable t) {
                Toast.makeText(ExerciseList.this, "Neuspjeh: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void searchList(String text) {
        ArrayList<Exercise> searchList = new ArrayList<>();
        for (Exercise exercise : dataList) {
            if (exercise.getName().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(exercise);
            }
        }
        adapter.searchDataList(searchList);
    }
}

