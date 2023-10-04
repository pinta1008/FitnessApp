package com.example.fitnessapp.AllForUsers;

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

public class UserCategoryAdapter extends RecyclerView.Adapter<UserCategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;

    public UserCategoryAdapter(Context context) {
        this.context = context;
    }

    public void setCategoryList(List<Category>categoryList)
    {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_category_recycler_item,parent, false);

        return new UserCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.catTitle.setText(categoryList.get(position).getName());
        byte[] imageBytes = Base64.decode(categoryList.get(position).getPhoto(), Base64.DEFAULT);


        holder.catImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));

        holder.catCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userExerciseList = new Intent(context, UserExerciseList.class);
                userExerciseList.putExtra("CategoryId", categoryList.get(position).getId());
                context.startActivity(userExerciseList);
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

class UserCategoryViewHolder extends RecyclerView.ViewHolder
{
    ImageView catImage;
    TextView catTitle;
    CardView catCard;
    ImageView deleteImg;

    public UserCategoryViewHolder(@NonNull View itemView)
    {
        super(itemView);
        catImage = itemView.findViewById(R.id.catImage);
        catTitle = itemView.findViewById(R.id.catTitle);
        catCard = itemView.findViewById(R.id.catCard);
        deleteImg = itemView.findViewById(R.id.deleteImg);
    }

}
