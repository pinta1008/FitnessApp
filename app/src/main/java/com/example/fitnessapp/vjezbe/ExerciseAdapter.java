package com.example.fitnessapp.vjezbe;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.models.Category;
import com.example.fitnessapp.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Exercise> dataList;

    public ExerciseAdapter(Context context, List<Exercise> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Exercise data = dataList.get(position);
        Category category = data.getCategory();
        byte[] imageBytes = Base64.decode(data.getPhoto(), Base64.DEFAULT);


        holder.recImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        holder.recTitle.setText(data.getName());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", data.getPhoto());
                intent.putExtra("Description", data.getDescription());
                intent.putExtra("Title", data.getName());
                intent.putExtra("Id", data.getId());
                intent.putExtra("Info", data.getInfo());
                intent.putExtra("categoryName", category.getName());
                intent.putExtra("Weight",data.getWeight());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<Exercise> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }
}


    class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recTitle = itemView.findViewById(R.id.recTitle);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
