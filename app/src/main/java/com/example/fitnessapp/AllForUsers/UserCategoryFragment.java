package com.example.fitnessapp.AllForUsers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.StatisticsActivity;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.models.Category;
import com.example.fitnessapp.R;
import com.example.fitnessapp.upload.UploadReviewActivity;
import com.example.fitnessapp.vjezbe.ExerciseMaxWeightActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserCategoryFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<Category> categoryList;

    private ImageButton statBtn;

    private ImageButton exeBtn;

    private ImageButton infoBtn;
    SearchView searchView;
    private UserCategoryAdapter userCategoryAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_category, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewUserCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        infoBtn = root.findViewById(R.id.infoBtn);

        statBtn = root.findViewById(R.id.statBtn);

        exeBtn = root.findViewById(R.id.exeBtn);

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UploadReviewActivity.class));
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
                startActivity(new Intent(getActivity(), ExerciseMaxWeightActivity.class));
            }
        });

        searchView = root.findViewById(R.id.SearchBar);

        searchView.clearFocus();

        userCategoryAdapter = new UserCategoryAdapter(getActivity());
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
                        userCategoryAdapter.setCategoryList(categoryList);
                        recyclerView.setAdapter(userCategoryAdapter);
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
        userCategoryAdapter.searchDataList(searchList);
    }

}