package com.example.fitnessapp.trening;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.example.fitnessapp.R;
import com.example.fitnessapp.interfaces.TrainingKreiranjeInterface;
import com.example.fitnessapp.models.ModelTraining;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingListAdapter extends RecyclerView.Adapter<HolderTrainingList> {

    private Context context;
    private ArrayList<ModelTraining> modelTrainings;

    private TrainingKreiranjeInterface trainingKreiranjeInterface;

    public TrainingListAdapter(Context context, ArrayList<ModelTraining> modelTrainingLists,TrainingKreiranjeInterface trainingKreiranjeInterface) {
        this.context = context;
        this.modelTrainings = modelTrainingLists;
        this.trainingKreiranjeInterface = trainingKreiranjeInterface;
    }

    @NonNull
    @Override
    public HolderTrainingList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_training_list_item, parent, false);

        return new HolderTrainingList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTrainingList holder, @SuppressLint("RecyclerView") int position) {


        //get data
        ModelTraining modelTrainingList = modelTrainings.get(position);
        Integer trainingId = modelTrainingList.getId();
        String trainingName = modelTrainingList.getName();
        String trainingBy = modelTrainingList.getUserId();
        String datum = modelTrainingList.getCreatedDate().toString();



        byte[] imageBytes = Base64.decode(modelTrainingList.getPhoto(), Base64.DEFAULT);

        //set data
        holder.trListTitle.setText(trainingName);
        holder.trListImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        holder.trListDate.setText(datum);




        holder.trListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //trebam id trainingId
                Intent intent = new Intent(context, TrainingListDetails.class);
                intent.putExtra("id", trainingId);
                intent.putExtra("name", trainingName);
                intent.putExtra("userId", trainingBy);
                context.startActivity(intent); //dohvati na TrainingListDetails

            }
        });


    }

    @Override
    public int getItemCount() {
        return modelTrainings.size();
    }

    private void deleteTraining(int position) {
        Integer treningId = modelTrainings.get(position).getId();
        Call<Void> call = trainingKreiranjeInterface.deleteTrening(treningId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    modelTrainings.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, modelTrainings.size());
                    Toast.makeText(context, "Trening obrisan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Ne moze obrisat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed to delete training: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
class HolderTrainingList extends RecyclerView.ViewHolder
{

    ImageView trListImage;
     TextView trListTitle;
     TextView trListDate;
     CardView trListCard;

    public HolderTrainingList(@NonNull View itemView) {
        super(itemView);
        trListImage = itemView.findViewById(R.id.trListImage);
        trListTitle = itemView.findViewById(R.id.trListTitle);
        trListCard = itemView.findViewById(R.id.trListCard);
        trListDate = itemView.findViewById(R.id.trListDate);
    }
}
