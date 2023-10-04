package com.example.fitnessapp.upload;

import static android.app.Activity.RESULT_OK;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessapp.interfaces.ExerciseInterface;
import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.RetrofitService;
import com.example.fitnessapp.models.Category;
import com.example.fitnessapp.models.Exercise;
import com.example.fitnessapp.models.ExerciseRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadFragment extends Fragment {

    View layoutView;
    ImageView uploadImage;
    Button saveButton;
    EditText uploadNapomena, uploadDesc, uploadExercise, uploadWeight;
    TextView uploadCategory;
    String imageURL;
    Uri uri;
    String currentUser;

    private List<Category> categories = new ArrayList<>();

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

    Category kategorija;

    ExerciseInterface exerciseInterface;

    IFitnessApi iFitnessApi;

    private ProgressDialog progressDialog;

    long maxid =  0;



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
       layoutView = inflater.inflate(R.layout.fragment_upload, container, false);

        uploadImage = layoutView.findViewById(R.id.uploadImage);
        uploadCategory = layoutView.findViewById(R.id.uploadCategory);
        uploadNapomena = layoutView.findViewById(R.id.uploadNapomena);
        uploadDesc = layoutView.findViewById(R.id.uploadDesc);
        uploadWeight = layoutView.findViewById(R.id.uploadWeight);
        uploadExercise  = layoutView.findViewById(R.id.uploadExercise);
        saveButton = layoutView.findViewById(R.id.saveButton);



        Retrofit retrofit = RetrofitService.getClient();
        exerciseInterface = retrofit.create(ExerciseInterface.class);

        iFitnessApi = retrofit.create(IFitnessApi.class);



        uploadNapomena.setKeyListener(DigitsKeyListener.getInstance("Q W E R T Z U I O P Š Đ A S D F G H J K L Č Ć Ž Y X C V B N M q w e r t z u i o p š đ a s d" +
                "f g h j k l č ć ž y x c v b n m "));
        uploadNapomena.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        uploadDesc.setKeyListener(DigitsKeyListener.getInstance("Q W E R T Z U I O P Š Đ A S D F G H J K L Č Ć Ž Y X C V B N M q w e r t z u i o p š đ a s d" +
                "f g h j k l č ć ž y x c v b n m "));
        uploadDesc.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        uploadExercise.setKeyListener(DigitsKeyListener.getInstance("Q W E R T Z U I O P Š Đ A S D F G H J K L Č Ć Ž Y X C V B N M q w e r t z u i o p š đ a s d" +
                "f g h j k l č ć ž y x c v b n m "));
        uploadExercise.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        uploadWeight.setRawInputType(InputType.TYPE_CLASS_NUMBER);




        uploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<Category>> call = iFitnessApi.getCategory();
                call.enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        if(response.isSuccessful())
                        {
                            categories = response.body();
                            categoryPickDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {

                    }
                });
            }
        });


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();


            }
        });
        return layoutView;
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

    private String selectedCategoryName;
    private int  selectedCategoryId;
    private void categoryPickDialog() {


        String[] categoriesArray = new String[categories.size()];
        for(int i = 0; i< categories.size(); i++)
        {
            categoriesArray[i] =  categories.get(i).getName();
        }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Odaberi kategoriju")
                    .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

                            Category selectedCategory = categories.get(which);

                            selectedCategoryName = selectedCategory.getName();
                            selectedCategoryId = selectedCategory.getId();
                            uploadCategory.setText(selectedCategoryName);

                        }
                    })
                    .show();

    }

    public void validateData()
    {

        int ponavljanja = 0;
        int serije = 0;
        int ukupno = serije * ponavljanja;


        //get data
         String desc = uploadDesc.getText().toString().trim();
         String exerc = uploadExercise.getText().toString().trim();
         String  nap = uploadNapomena.getText().toString().trim();
         String weight = uploadWeight.getText().toString().trim();

        //validate data
        if(TextUtils.isEmpty(exerc))
        {
            Toast.makeText(getActivity(), "Upiši vježbu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(weight) || Integer.parseInt(weight) != 0 && Integer.parseInt(weight) != 1)
        {
            Toast.makeText(getActivity(), "Možete napisati samo 0 ili 1", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(desc))
        {
            Toast.makeText(getActivity(), "Upiši opis", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(nap))
        {
            Toast.makeText(getActivity(), "Upiši informaciju", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(selectedCategoryName))
        {
            Toast.makeText(getActivity(), "Izaberi kategoriju", Toast.LENGTH_SHORT).show();
        }
        else if(imageUri == null)
        {
            Toast.makeText(getActivity(), "Izaberite sliku ...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploadExerciseInfoToDb(exerc,desc,nap,ponavljanja,serije,ukupno, Integer.parseInt(weight));
        }

    }

    private void uploadExerciseInfoToDb(String exerciseName, String desc, String info, int ponavljanja, int serije, int ukupno, int weight)
    {

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

        ExerciseRequest exerciseRequest = new ExerciseRequest(exerciseName,desc,info,imgBase64,ponavljanja,serije,ukupno,selectedCategoryId,weight);

        Call<Exercise> call = exerciseInterface.addExercise(exerciseRequest);
        call.enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "Vjezba spremljena", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(), "Vjezba nije spremljena jer vježba s istim imenom već postoji.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                Toast.makeText(getActivity(), "Ne dela", Toast.LENGTH_SHORT).show();
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
                uploadImage.setImageURI(imageUri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // image picked from camera, get uri of image
                uploadImage.setImageURI(imageUri);
            }
        }
    }

}