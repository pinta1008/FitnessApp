package com.example.fitnessapp.trening;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.interfaces.TrainingKreiranjeInterface;
import com.example.fitnessapp.models.ModelTraining;
import com.example.fitnessapp.models.SingletonUser;
import com.example.fitnessapp.trening.TrainingListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrListFragment extends Fragment {

    View layoutView;
    RecyclerView recyclerView;
    ArrayList<ModelTraining> modelTrainingLists;
    TrainingListAdapter adapter;

    private TrainingKreiranjeInterface trainingKreiranjeInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_tr_list, container, false);
        recyclerView = layoutView.findViewById(R.id.recyclerViewTrList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);




        modelTrainingLists = new ArrayList<>();

        trainingKreiranjeInterface = RetrofitService.getClient().create(TrainingKreiranjeInterface.class);

        adapter = new TrainingListAdapter(getContext(), modelTrainingLists, trainingKreiranjeInterface);
        recyclerView.setAdapter(adapter);



        loadTrening();

        return layoutView;
    }

    private void loadTrening() {
        Call<List<ModelTraining>> call = trainingKreiranjeInterface.getTrainings(SingletonUser.getInstance().getId());
        call.enqueue(new Callback<List<ModelTraining>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelTraining>> call, @NonNull Response<List<ModelTraining>> response) {
                if (response.isSuccessful()) {
                    List<ModelTraining> exerciseList = response.body();
                    if (exerciseList != null) {
                        modelTrainingLists.clear();

                        // Format the createdDate property for each ModelTraining object
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

                        for (ModelTraining training : exerciseList) {
                            try {
                                Date date = inputFormat.parse(training.getCreatedDate());
                                String formattedDate = outputFormat.format(date);
                                training.setCreatedDate(formattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        modelTrainingLists.addAll(exerciseList);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "Neuspješno očitavanje treninga", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelTraining>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Neuspješno: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}