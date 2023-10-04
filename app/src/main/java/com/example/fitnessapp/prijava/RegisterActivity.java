package com.example.fitnessapp.prijava;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.AllForUsers.DashboardUserActivity;
import com.example.fitnessapp.interfaces.LoginInterface;
import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.databinding.ActivityRegisterBinding;
import com.example.fitnessapp.models.RegisterRequest;
import com.example.fitnessapp.models.RegisterResponse;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private ImageView profilePic;
    private ProgressDialog progressDialog;
    private String imgBase64;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profilePic = findViewById(R.id.profilePic);
        //init permission
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        binding.etIme.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"));
        binding.etIme.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Molimo pričekajte");
        progressDialog.setCanceledOnTouchOutside(false);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pick image
                showImagePickDialog();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        binding.tvLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
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
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

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

    private void validateData() {
        //get data
        String username = binding.etIme.getText().toString().trim();
        String password = binding.etRegPass.getText().toString().trim();
        String email = binding.etRegEmail.getText().toString().trim();
        String passConf = binding.etRegPassConf.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Unesite korisničko ime...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Unesite lozinku...", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Lozinka mora sadržavati barem 6 karaktera...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Unesite email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(passConf)){
            Toast.makeText(this, "Ne može biti prazno!", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(passConf)) {
            Toast.makeText(this, "Lozinka se ne podudara!", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Nevažeći format email adrese...", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Toast.makeText(this, "Izaberite sliku profila...", Toast.LENGTH_SHORT).show();
        } else {
            //data is valid, proceed to register user
            registerUser(username, password, email);
        }
    }

    private void registerUser(String username, String password, String email) {
        progressDialog.setMessage("Registracija u toku...");
        progressDialog.show();

        // Convert the image to Base64
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            imgBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, "Greška pri konverziji slike.", Toast.LENGTH_SHORT).show();
        }

        // Create a RegisterRequest object with the user details
        RegisterRequest registerRequest = new RegisterRequest(username, password, email, imgBase64);

        // Create an instance of the Retrofit service
        LoginInterface loginInterface = RetrofitService.getClient().create(LoginInterface.class);

        // Make the API call to register the user
        Call<RegisterResponse> call = loginInterface.registerUser(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    // Registration successful
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        Toast.makeText(RegisterActivity.this, "Registracija uspješna.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, DashboardUserActivity.class));
                        finish();
                    }
                } else {
                    // Registration failed
                    Toast.makeText(RegisterActivity.this, "Registracija nije uspjela.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Greška: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // image picked from gallery, get uri of image
                imageUri = data.getData();
                // set to imageview
                profilePic.setImageURI(imageUri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // image picked from camera, get uri of image
                profilePic.setImageURI(imageUri);
            }
        }
    }
}
