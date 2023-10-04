package com.example.fitnessapp.trening;

import com.example.fitnessapp.R;
import com.example.fitnessapp.interfaces.TrainingKreiranjeInterface;
import com.example.fitnessapp.models.Exercise;
import com.example.fitnessapp.models.ExerciseUser;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingAdapter extends RecyclerView.Adapter<HolderTraining> {

    private Context context;
    private ArrayList<ExerciseUser> exerciseUsers;
    private TrainingKreiranjeInterface trainingInterface;

    public TrainingAdapter(Context context, ArrayList<ExerciseUser> exerciseUsers, TrainingKreiranjeInterface trainingInterface) {
        this.context = context;
        this.exerciseUsers = exerciseUsers;
        this.trainingInterface = trainingInterface;
    }

    @NonNull
    @Override
    public HolderTraining onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_training_item, parent, false);
        return new HolderTraining(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTraining holder, @SuppressLint("RecyclerView") int position) {
        ExerciseUser exerciseUser = exerciseUsers.get(position);
        Exercise exercise = exerciseUser.getExercise();
        int id = exerciseUser.getId();
        String exerciseName = exercise.getName();
       // String exercisePhoto = exercise.getPhoto();
        int ponavljanja = exerciseUser.getNum_pon();
        int serije = exerciseUser.getNum_ser();
        int ukupno = exerciseUser.getNum_uk();

        int tezina_kg = exerciseUser.getWeight();

        byte[] imageBytes = Base64.decode(exercise.getPhoto(), Base64.DEFAULT);


        holder.trImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));



        holder.exePonavljanja.setText(String.valueOf(ponavljanja));
        holder.exeSerije.setText(String.valueOf(serije));
        holder.exeUkupno.setText(String.valueOf(ukupno));
        holder.exeWeight.setText(String.valueOf(tezina_kg));
        holder.exeTitle.setText(exerciseName);

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTraining(id, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseUsers.size();
    }

    private void deleteTraining(int treningId, int position) {
        Call<Void> call = trainingInterface.deleteFromTraining(treningId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    exerciseUsers.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Vježba obrisana", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Vježba nije obrisana", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed to delete training: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class HolderTraining extends RecyclerView.ViewHolder {
    public TextView exeTitle, exePonavljanja, exeSerije, exeUkupno, exeWeight;
    public ImageView trImage;
    public CardView trCard;
    public ImageButton deleteImg;

    public HolderTraining(@NonNull View itemView) {
        super(itemView);

        exeTitle = itemView.findViewById(R.id.exeTitle);
        exePonavljanja = itemView.findViewById(R.id.exePonavljanja);
        exeSerije = itemView.findViewById(R.id.exeSerije);
        exeUkupno = itemView.findViewById(R.id.exeUkupno);
        exeWeight = itemView.findViewById(R.id.exeWeight);
        trImage = itemView.findViewById(R.id.trImage);
        trCard = itemView.findViewById(R.id.trCard);
        deleteImg = itemView.findViewById(R.id.deleteImg);
    }
}
