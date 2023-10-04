package com.example.fitnessapp.kategorije;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Review.ReviewActivity;
import com.example.fitnessapp.StatisticsActivity;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.models.Category;
import com.example.fitnessapp.models.ExerciseMaxWeightWithUsername;
import com.example.fitnessapp.upload.UploadReviewActivity;
import com.example.fitnessapp.vjezbe.ExerciseMaxWeightActivity;
import com.example.fitnessapp.vjezbe.ExerciseMaxWeightUsernameActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<Category> categoryList;

    private ImageButton statBtn;

    private ImageButton exeBtn;

     ImageButton infoBtn;
    SearchView searchView;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        searchView = root.findViewById(R.id.SearchBar);

        searchView.clearFocus();

        infoBtn = root.findViewById(R.id.infoBtn);

        statBtn = root.findViewById(R.id.statBtn);

        exeBtn = root.findViewById(R.id.exeBtn);

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ReviewActivity.class));
            }
        });

        statBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), StatisticsActivity.class));
            }
        });
        exeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ExerciseMaxWeightUsernameActivity.class));
            }
        });


        categoryAdapter = new CategoryAdapter(getActivity()); // Initialize the adapter

        // Get Retrofit instance from RetrofitService
        Retrofit retrofit = RetrofitService.getClient();

        // Create an instance of the Retrofit service interface
        IFitnessApi fitnessApi = retrofit.create(IFitnessApi.class);

        // Make an API request to get categories
        Call<List<Category>> call = fitnessApi.getCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoryList = response.body();
                    if (categoryList != null) {
                        categoryAdapter.setCategoryList(categoryList);
                        recyclerView.setAdapter(categoryAdapter);
                    }
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                // Handle network failure
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        return root;
    }
    public void searchList(String text)
    {
        ArrayList<Category> searchList = new ArrayList<>();
        for(Category category : categoryList)
        {
            if(category.getName().toLowerCase().contains(text.toLowerCase()))
            {
                searchList.add(category);
            }
        }
        categoryAdapter.searchDataList(searchList);
    }
}

