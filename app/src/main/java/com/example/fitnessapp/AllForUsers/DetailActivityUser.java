package com.example.fitnessapp.AllForUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.interfaces.ExerciseInterface;
import com.example.fitnessapp.interfaces.TrainingKreiranjeInterface;
import com.example.fitnessapp.models.Exercise;
import com.example.fitnessapp.R;
import com.example.fitnessapp.models.ExerciseUser;
import com.example.fitnessapp.models.ExerciseUserRequest;
import com.example.fitnessapp.models.SingletonUser;
import com.example.fitnessapp.vjezbe.DetailActivity;
import com.example.fitnessapp.vjezbe.EditExerciseActivity;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivityUser extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailInfo, detailCategory;
    ImageView detailImage;
    Button addExe;

    String imageUrl = "";

    int tezina_kg;
    Exercise exercise;

    ExerciseUser exerciseUser;
    ExerciseUserRequest exerciseUserRequest;

    ExerciseInterface exerciseInterface;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        detailDesc = findViewById(R.id.detailDesc);
        detailTitle = findViewById(R.id.detailTitle);
        detailInfo = findViewById(R.id.detailInfo);
        detailCategory = findViewById(R.id.detailCategory);
        detailImage = findViewById(R.id.detailImage);
        addExe = findViewById(R.id.add_trening_btn);

        Retrofit retrofit = RetrofitService.getClient();
        exerciseInterface = retrofit.create(ExerciseInterface.class);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailInfo.setText(bundle.getString("Info"));
            detailCategory.setText(bundle.getString("categoryName"));
            imageUrl = bundle.getString("Image");
            tezina_kg = bundle.getInt("Weight");

            byte[] imageBytes = Base64.decode(imageUrl, Base64.DEFAULT);
            detailImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        }

        addExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(tezina_kg == 1)
                {
                    showQuantityDialog(exercise);
                }
                else if(tezina_kg == 0)
                {
                    showQuantityDialog2(exercise);
                }

            }
        });

    }

    private int br_ponavljanja = 0;
    private int br_serija = 0;
    private int final_cost = 0;

    private void showQuantityDialog(Exercise exercise) {
        // Inflate layout for dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_quantity, null);
        ImageView exercisePhoto = view.findViewById(R.id.exercisePhoto);
        TextView exerciseName = view.findViewById(R.id.exeTitle);
        ImageButton desPonavljanja = view.findViewById(R.id.desBtn);
        TextView brojPonavljanja = view.findViewById(R.id.broj_ponavljanja);
        ImageButton insPonavljanja = view.findViewById(R.id.insBtn);
        ImageButton desSerije = view.findViewById(R.id.desBtnSerije);
        TextView brojSerija = view.findViewById(R.id.broj_serija);
        ImageButton insSerija = view.findViewById(R.id.insBtnSerije);
        EditText weight = view.findViewById(R.id.weight);

        weight.setRawInputType(InputType.TYPE_CLASS_NUMBER);




        // Button in dialog
        Button contButton = view.findViewById(R.id.contBtn);

        // Get data from DataClass
        Bundle bundle = getIntent().getExtras();
        String exerciseTitle = bundle.getString("Title");
        String exerciseImg = bundle.getString("Image");



        br_ponavljanja = 1;
        br_serija = 1;
        final_cost = br_ponavljanja * br_serija;

        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        if (!exerciseImg.isEmpty()) {
            byte[] imageBytes = Base64.decode(exerciseImg, Base64.DEFAULT);
            exercisePhoto.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        }

        exerciseName.setText("" + exerciseTitle);
        brojPonavljanja.setText("" + br_ponavljanja);
        brojSerija.setText("" + br_serija);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Increase number of repetitions
        insPonavljanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                br_ponavljanja++;
                brojPonavljanja.setText(""+br_ponavljanja);
            }
        });

        // Decrease number of repetitions
        desPonavljanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (br_ponavljanja > 1) {
                    br_ponavljanja--;
                    brojPonavljanja.setText(""+br_ponavljanja);
                }
            }
        });

        insSerija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                br_serija++;
                brojSerija.setText(""+br_serija);
            }
        });

        // Decrease number of series
        desSerije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (br_serija > 1) {
                    br_serija--;
                    brojSerija.setText(""+br_serija);
                }
            }
        });

        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ponavljanja = br_ponavljanja;
                int serije = br_serija;
                int ukupno = br_ponavljanja * br_serija;
                int exerciseID = bundle.getInt("Id");
                String tezina = weight.getText().toString().trim();
                String id = SingletonUser.getInstance().getId();
                Integer treningId = SingletonUser.getInstance().getTreningId();


                exerciseUserRequest = new ExerciseUserRequest(
                        id,
                        ponavljanja,
                        serije,
                        ukupno,
                        exerciseID,
                        treningId,
                        Integer.parseInt(tezina)
                );

                progressDialog = new ProgressDialog(DetailActivityUser.this);
                progressDialog.setMessage("Dodavanje");
                progressDialog.setCancelable(false);
                progressDialog.show();


                addExerciseToTraining(exerciseUserRequest);



                // Add to database
                progressDialog.dismiss();
                dialog.dismiss();
            }
        });
    }
    private void showQuantityDialog2(Exercise exercise) {
        // Inflate layout for dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_quantity2, null);
        ImageView exercisePhoto = view.findViewById(R.id.exercisePhoto);
        TextView exerciseName = view.findViewById(R.id.exeTitle);
        ImageButton desPonavljanja = view.findViewById(R.id.desBtn);
        TextView brojPonavljanja = view.findViewById(R.id.broj_ponavljanja);
        ImageButton insPonavljanja = view.findViewById(R.id.insBtn);
        ImageButton desSerije = view.findViewById(R.id.desBtnSerije);
        TextView brojSerija = view.findViewById(R.id.broj_serija);
        ImageButton insSerija = view.findViewById(R.id.insBtnSerije);


        // Button in dialog
        Button contButton = view.findViewById(R.id.contBtn);

        // Get data from DataClass
        Bundle bundle = getIntent().getExtras();
        String exerciseTitle = bundle.getString("Title");
        String exerciseImg = bundle.getString("Image");



        br_ponavljanja = 1;
        br_serija = 1;
        final_cost = br_ponavljanja * br_serija;

        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        if (!exerciseImg.isEmpty()) {
            byte[] imageBytes = Base64.decode(exerciseImg, Base64.DEFAULT);
            exercisePhoto.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        }

        exerciseName.setText("" + exerciseTitle);
        brojPonavljanja.setText("" + br_ponavljanja);
        brojSerija.setText("" + br_serija);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Increase number of repetitions
        insPonavljanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                br_ponavljanja++;
                brojPonavljanja.setText(""+br_ponavljanja);
            }
        });

        // Decrease number of repetitions
        desPonavljanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (br_ponavljanja > 1) {
                    br_ponavljanja--;
                    brojPonavljanja.setText(""+br_ponavljanja);
                }
            }
        });

        insSerija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                br_serija++;
                brojSerija.setText(""+br_serija);
            }
        });

        // Decrease number of series
        desSerije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (br_serija > 1) {
                    br_serija--;
                    brojSerija.setText(""+br_serija);
                }
            }
        });

        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ponavljanja = br_ponavljanja;
                int serije = br_serija;
                int ukupno = br_ponavljanja * br_serija;
                int exerciseID = bundle.getInt("Id");
                String id = SingletonUser.getInstance().getId();
                Integer treningId = SingletonUser.getInstance().getTreningId();


                exerciseUserRequest = new ExerciseUserRequest(
                        id,
                        ponavljanja,
                        serije,
                        ukupno,
                        exerciseID,
                        treningId,
                        0
                );

                progressDialog = new ProgressDialog(DetailActivityUser.this);
                progressDialog.setMessage("Dodavanje");
                progressDialog.setCancelable(false);
                progressDialog.show();


                addExerciseToTraining(exerciseUserRequest);



                // Add to database
                progressDialog.dismiss();
                dialog.dismiss();
            }
        });
    }
    private void addExerciseToTraining(ExerciseUserRequest exerciseUserRequest) {

        Retrofit retrofit = RetrofitService.getClient();

        TrainingKreiranjeInterface trainingKreiranjeInterface = retrofit.create(TrainingKreiranjeInterface.class);

        Call<ExerciseUser> call = trainingKreiranjeInterface.addToTrainingKosara(exerciseUserRequest);
        call.enqueue(new Callback<ExerciseUser>() {
            @Override
            public void onResponse(Call<ExerciseUser> call, Response<ExerciseUser> response) {
                if(response.isSuccessful())
                {

                    if(SingletonUser.getInstance().getTreningId() == null)
                    {
                        SingletonUser.getInstance().setTreningId(response.body().getTreningId());
                    }
                    Toast.makeText(DetailActivityUser.this, "Uspješno", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(DetailActivityUser.this, "Vježba već postoji", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExerciseUser> call, Throwable t) {
                Toast.makeText(DetailActivityUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}