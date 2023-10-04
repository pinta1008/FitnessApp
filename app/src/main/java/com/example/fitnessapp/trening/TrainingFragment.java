package com.example.fitnessapp.trening;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.interfaces.TrainingKreiranjeInterface;
import com.example.fitnessapp.models.ExerciseUser;
import com.example.fitnessapp.models.ModelTraining;
import com.example.fitnessapp.models.SingletonUser;
import com.example.fitnessapp.models.TreningRequest;
import com.example.fitnessapp.trening.TrainingAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TrainingFragment extends Fragment {

    private RecyclerView recyclerView;

    //permissions
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    //image constant
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    //string
    private String[] cameraPermissions;
    private String[] storagePermissions;

    //image picked uri
    private Uri imageUri;

    private ProgressDialog progressDialog;
    ImageView uploadCatImg;
    EditText uploadTrName;

    private String imgBase64;

    private TrainingAdapter trainingAdapter;
    private ArrayList<ExerciseUser> exerciseUsers;

    private TrainingKreiranjeInterface trainingInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_training, container, false);

        recyclerView = root.findViewById(R.id.trainingItems);

        uploadTrName = root.findViewById(R.id.trainingTitle);
        uploadCatImg = root.findViewById(R.id.trImage);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Molimo pričekajte");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Button checkoutBtn = root.findViewById(R.id.checkoutBtn);


        uploadCatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });



        exerciseUsers = new ArrayList<>();

        Retrofit retrofit = RetrofitService.getClient();
        trainingInterface = retrofit.create(TrainingKreiranjeInterface.class);

        trainingAdapter = new TrainingAdapter(getContext(), exerciseUsers, trainingInterface);
        recyclerView.setAdapter(trainingAdapter);

        // Fetch training data from the server
        fetchTrainingData();

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        return root;
    }

    private void showImagePickDialog() {
        //Options to display in dialog
        String[] options = {"Kamera", "Galerija"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Izaberi sliku").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //handle clicks
                if (which == 0) {
                    //camera clicked
                    if (checkCameraPermission()) {
                        pickFromCamera();
                    } else {
                        requestCameraPermission();
                    }
                } else {
                    //gallery clicked
                    if (checkStoragePermission()) {
                        pickFromGallery();
                    } else {
                        requestStoragePermission();
                    }
                }
            }
        }).show();
    }

    private void pickFromGallery() {
        //pick image from gallery using intent
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //intent to pick image from camera, the image will be returned in onActivityResult method
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Slika naslova");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Slika opisa");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        //request runtime storage permission
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //check if camera permission is enabled or not
        boolean resultCamera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean resultStorage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return resultCamera && resultStorage;
    }

    private void requestCameraPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }



    private void fetchTrainingData() {
        Call<ModelTraining> call = trainingInterface.getKosara(SingletonUser.getInstance().getId());
        call.enqueue(new Callback<ModelTraining>() {
            @Override
            public void onResponse(Call<ModelTraining> call, Response<ModelTraining> response) {
                if (response.isSuccessful()) {
                    ModelTraining modelTraining = response.body();
                    if(modelTraining != null)
                    {
                        List<ExerciseUser> exerciseUserList = modelTraining.getVjezbe();
                        exerciseUsers.clear();
                        exerciseUsers.addAll(exerciseUserList);
                        trainingAdapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(getContext(), "Neuspješno očitavanje podataka", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelTraining> call, Throwable t) {
                Toast.makeText(getContext(), "Neuspješno očitavanje podataka: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void validateData() {
        //get data
        String name = uploadTrName.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Unesi ime...", Toast.LENGTH_SHORT).show();

        } else if (imageUri == null) {
            Toast.makeText(getActivity(), "Izaberite sliku ...", Toast.LENGTH_SHORT).show();
        } else {
            //data is valid, proceed to register user
            saveTrainingData(name);
        }
    }

    private void saveTrainingData(String name) {


        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            imgBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Greška pri konverziji slike.", Toast.LENGTH_SHORT).show();
        }


        TreningRequest training = new TreningRequest(SingletonUser.getInstance().getTreningId(), name, imgBase64);

        Call<ModelTraining> call = trainingInterface.addToTraining(training);
        call.enqueue(new Callback<ModelTraining>() {
            @Override
            public void onResponse(Call<ModelTraining> call, Response<ModelTraining> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "Trening spremljen", Toast.LENGTH_SHORT).show();
                    SingletonUser.getInstance().setTreningId(null);
                }
                else
                {
                    Toast.makeText(getActivity(), "Training nije spremljen.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ModelTraining> call, Throwable t) {
                Toast.makeText(getActivity(), "Training data not saved successfully.", Toast.LENGTH_SHORT).show();
            }
        });



    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                // picking from camera, first check if camera and storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        // both permissions are granted
                        pickFromCamera();
                    } else {
                        // either camera or storage or both permissions denied
                        Toast.makeText(getActivity(), "Potrebne su dozvole za kameru i pohranu...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                // picking from gallery, first check if storage permission allowed or not
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        // storage permission granted
                        pickFromGallery();
                    } else {
                        // storage permission denied
                        Toast.makeText(getActivity(), "Potrebna je dozvola za pohranu...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // image picked from gallery, get uri of image
                imageUri = data.getData();
                // set to imageview
                uploadCatImg.setImageURI(imageUri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // image picked from camera, get uri of image
                uploadCatImg.setImageURI(imageUri);
            }
        }
    }


}
