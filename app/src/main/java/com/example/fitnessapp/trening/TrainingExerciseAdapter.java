package com.example.fitnessapp.trening;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.models.Exercise;
import com.example.fitnessapp.models.ExerciseUser;

import java.util.ArrayList;

public class TrainingExerciseAdapter extends RecyclerView.Adapter<HolderTrainingExercise> {

    private Context context;
    private ArrayList<ExerciseUser> exerciseList;

    private int treningID;

    public TrainingExerciseAdapter(Context context, ArrayList<ExerciseUser> exerciseList, int treningID) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.treningID = treningID;
    }

    @NonNull
    @Override
    public HolderTrainingExercise onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_my_training_exercise_item, parent, false);
        return new HolderTrainingExercise(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTrainingExercise holder, @SuppressLint("RecyclerView") int position) {

        // Get data
        ExerciseUser exerciseUser = exerciseList.get(position);
        Exercise exercise = exerciseUser.getExercise();
        String name = exercise.getName();
        int ponavljanja = exerciseUser.getNum_pon();
        int serije = exerciseUser.getNum_ser();
        int ukupno = exerciseUser.getNum_uk();

        int tezina_kg = exerciseUser.getWeight();

        byte[] imageBytes = Base64.decode(exercise.getPhoto(), Base64.DEFAULT);

        holder.trImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        holder.exeTitle.setText(name);
        holder.exePonavljanja.setText(String.valueOf(ponavljanja));
        holder.exeSerije.setText(String.valueOf(serije));
        holder.exeUkupno.setText(String.valueOf(ukupno));
        holder.exeWeight.setText(String.valueOf(tezina_kg));


    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public void searchDataList(ArrayList<ExerciseUser> searchList) {
        exerciseList = searchList;
        notifyDataSetChanged();
    }
}

class HolderTrainingExercise extends RecyclerView.ViewHolder {
    // Views of recycler_my_training_exercise_item
    ImageView trImage;
    TextView exeTitle;
    TextView exePonavljanja;
    TextView exeSerije;
    TextView exeUkupno;

    TextView exeWeight;
    ImageButton deleteImg;

    public HolderTrainingExercise(@NonNull View itemView) {
        super(itemView);

        // Init
        trImage = itemView.findViewById(R.id.trImage);
        exeTitle = itemView.findViewById(R.id.exeTitle);
        exePonavljanja = itemView.findViewById(R.id.exePonavljanja);
        exeSerije = itemView.findViewById(R.id.exeSerije);
        exeWeight = itemView.findViewById(R.id.exeWeight);
        exeUkupno = itemView.findViewById(R.id.exeUkupno);
        deleteImg = itemView.findViewById(R.id.deleteImg);
    }
}
