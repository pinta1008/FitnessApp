package com.example.fitnessapp.vjezbe;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.models.ExerciseMaxWeight;
import com.example.fitnessapp.models.ExerciseMaxWeightWithUsername;

import java.util.List;

public class ExerciseMaxWeightUserAdapter extends RecyclerView.Adapter<ExerciseMaxWeightUserAdapter.ViewHolder> {

    private List<ExerciseMaxWeightWithUsername> data;

    public ExerciseMaxWeightUserAdapter(List<ExerciseMaxWeightWithUsername> data) {
        this.data = data;
    }

    public void updateData(List<ExerciseMaxWeightWithUsername> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_max_weight_username, parent, false);
        return new ExerciseMaxWeightUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciseMaxWeightWithUsername item = data.get(position);
        byte[] imageBytes = Base64.decode(data.get(position).getExercisePhoto(), Base64.DEFAULT);


        holder.catImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catImage;
        private TextView catTitle;
        private TextView catName;
        private TextView catWeight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catTitle = itemView.findViewById(R.id.catTitle);
            catName = itemView.findViewById(R.id.catName);
            catWeight = itemView.findViewById(R.id.catWeight);
            catImage = itemView.findViewById(R.id.catImage);
        }

        public void bind(ExerciseMaxWeightWithUsername item) {
            catTitle.setText(item.getUsername());
            catName.setText(item.getExerciseName());
            catWeight.setText("Maksimalna težina: " + item.getMaxWeight() + " kg");
        }
    }
}
