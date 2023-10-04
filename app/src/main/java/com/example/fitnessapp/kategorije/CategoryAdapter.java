package com.example.fitnessapp.kategorije;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnessapp.vjezbe.ExerciseList;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.models.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context)
    {
        this.context = context;

    }
    public void setCategoryList(List<Category>categoryList)
    {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_category_item,parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.catTitle.setText(categoryList.get(position).getName());
        byte[] imageBytes = Base64.decode(categoryList.get(position).getPhoto(), Base64.DEFAULT);


        holder.catImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));

        holder.catCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exerciseList = new Intent(context, ExerciseList.class);
                exerciseList.putExtra("CategoryId", categoryList.get(position).getId());
                context.startActivity(exerciseList);
            }
        });
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deleteCategory(position);
            }
        });

    }
    public void deleteCategory(int position)
    {
        int categoryId = categoryList.get(position).getId();

        Retrofit retrofit = RetrofitService.getClient();
        IFitnessApi fitnessApi = retrofit.create(IFitnessApi.class);

        Call<Void> call = fitnessApi.deleteCategory(String.valueOf(categoryId));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    categoryList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, categoryList.size());
                    Toast.makeText(context, "Kategorija Obrisana", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Neuspješno brisanje", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void searchDataList(ArrayList<Category> searchList)
    {
        categoryList = searchList;
        notifyDataSetChanged();
    }

}



class CategoryViewHolder extends RecyclerView.ViewHolder
{
    ImageView catImage;
    TextView catTitle;
    CardView catCard;
    ImageView deleteImg;

    public CategoryViewHolder(@NonNull View itemView)
    {
        super(itemView);
        catImage = itemView.findViewById(R.id.catImage);
        catTitle = itemView.findViewById(R.id.catTitle);
        catCard = itemView.findViewById(R.id.catCard);
        deleteImg = itemView.findViewById(R.id.deleteImg);
    }


}
