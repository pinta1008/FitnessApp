package com.example.fitnessapp.vjezbe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.fitnessapp.interfaces.ExerciseInterface;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.databinding.ActivityEditExerciseBinding;
import com.example.fitnessapp.models.Category;
import com.example.fitnessapp.models.Exercise;
import com.example.fitnessapp.models.ExerciseRequest;
import com.example.fitnessapp.models.SingletonUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditExerciseActivity extends AppCompatActivity {

    //binding
    private ActivityEditExerciseBinding binding;

    private List<Category> categories = new ArrayList<>();
    IFitnessApi iFitnessApi;
    private  int exeId;

    private String imgBase64;

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

    //progress dialog
   private ProgressDialog progressDialog;

   ExerciseInterface exerciseInterface;

   private ArrayList<String> categoryTitleArrayList, categoryIdArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditExerciseBinding.inflate(getLayoutInflater());

        binding.etExeName.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"));
        binding.etExeName.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        binding.etExeDesc.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"));
        binding.etExeDesc.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        binding.etExeInfo.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"));
        binding.etExeInfo.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        setContentView(binding.getRoot());



        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {

        }

        exeId = bundle.getInt("dalje_Id");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Molimo pričekajte");
        progressDialog.setCanceledOnTouchOutside(false);
        loadCategories();
        loadExeInfo();

        //click za biranje kateogije
        binding.pickCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog();
            }
        });
        binding.exercisePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });

        //click za upload
        binding.btnUpdateExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();

            }
        });


    }

    private void showImagePickDialog() {
        //Options to display in dialog
        String[] options = {"Kamera", "Galerija"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //check if camera permission is enabled or not
        boolean resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean resultStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return resultCamera && resultStorage;
    }

    private void requestCameraPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private  String name ="",description="",info="";
    int photoChanger = 0;
    private void validateData() {
        name = binding.etExeName.getText().toString().trim();
        description = binding.etExeDesc.getText().toString().trim();
        info = binding.etExeInfo.getText().toString().trim();


        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Unesi ime!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "Unesi opis!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(info))
        {
            Toast.makeText(this, "Unesi info!", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(selectedCategoryName))
        {
            Toast.makeText(this, "Izaberi kategoriju", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(imageUri == null)
            {
                imgBase64 = "";
                updateExercise();
                Intent intent = new Intent(EditExerciseActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] imageBytes = baos.toByteArray();
                    imgBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    photoChanger = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(this, "Greška pri konverziji slike.", Toast.LENGTH_SHORT).show();
                }
                updateExercise();
                Intent intent = new Intent(EditExerciseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

    }

    private void updateExercise() {
        Retrofit retrofit = RetrofitService.getClient();
        exerciseInterface = retrofit.create(ExerciseInterface.class);

        ExerciseRequest exerciseRequest = new ExerciseRequest(name,description,info,imgBase64,selectedCategoryId,photoChanger);

        Call<Void> call = exerciseInterface.updateExercise(exeId, exerciseRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(EditExerciseActivity.this, "Uspješne promjene", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EditExerciseActivity.this, "Greška pri promjeni", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditExerciseActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadExeInfo() {

        Retrofit retrofit = RetrofitService.getClient();
        exerciseInterface = retrofit.create(ExerciseInterface.class);

        Call<Exercise> call = exerciseInterface.getOneExercise(SingletonUser.getInstance().getJwt(), exeId);
        call.enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                if(response.isSuccessful())
                {
                    byte[] imageBytes = Base64.decode(response.body().getPhoto(), Base64.DEFAULT);

                    binding.exercisePic.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                    binding.etExeName.setText(response.body().getName());
                    binding.etExeDesc.setText(response.body().getDescription());
                    binding.etExeInfo.setText(response.body().getInfo());
                    binding.pickCategory.setText(response.body().getCategory().getName());
                }
                else
                {
                    Toast.makeText(EditExerciseActivity.this, "Greška pri pozivu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                Toast.makeText(EditExerciseActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private String selectedCategoryName;
    private int  selectedCategoryId;
    private void categoryDialog() {


        String[] categoriesArray = new String[categories.size()];
        for(int i = 0; i< categories.size(); i++)
        {
            categoriesArray[i] =  categories.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Odaberi kategoriju")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        Category selectedCategory = categories.get(which);

                        selectedCategoryName = selectedCategory.getName();
                        selectedCategoryId = selectedCategory.getId();
                        binding.pickCategory.setText(selectedCategoryName);

                    }
                })
                .show();

    }

    private void loadCategories() {
        Retrofit retrofit = RetrofitService.getClient();
        iFitnessApi = retrofit.create(IFitnessApi.class);

        Call<List<Category>> call = iFitnessApi.getCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful())
                {
                    categories = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

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
                        Toast.makeText(this, "Potrebne su dozvole za kameru i pohranu...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Potrebna je dozvola za pohranu...", Toast.LENGTH_SHORT).show();
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
                binding.exercisePic.setImageURI(imageUri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // image picked from camera, get uri of image
                binding.exercisePic.setImageURI(imageUri);
            }
        }
    }
}