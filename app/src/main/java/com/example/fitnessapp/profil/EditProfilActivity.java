package com.example.fitnessapp.profil;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitnessapp.R;
import com.example.fitnessapp.databinding.ActivityEditProfilBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfilActivity extends AppCompatActivity {

    private ActivityEditProfilBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "PROFILE_EDIT_TAG";
    private Uri imageUri = null;
    private String name = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.etIme.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"));
        binding.etIme.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        //setup progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Molimo pričekajte");
        progressDialog.setCanceledOnTouchOutside(false);//dont dismiss while clicked

        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();


        //handle click, go back
       // binding.backBtn.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View view) {
              //  onBackPressed();
          //  }
      //  });

        //handle click pick image
        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageAttachMenu();
            }
        });

        //handle click update profile
        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {

        //get data
        name = binding.etIme.getText().toString().trim();
        //validate data
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Unesi ime", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(imageUri == null)
            {
                updateProfile("");
            }
            else
            {
                updateImage();
            }
        }

    }

    private void updateImage() {
        Log.d(TAG, "updateImage: Upload");
        progressDialog.setMessage("Ažuriranje slike profila");
        progressDialog.show();
        //image path and name, use uid to replace previous
        String filePathAndName = "Android Images/"+ firebaseAuth.getUid();
        //storage reference
        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: Profilna promjenjena");
                Log.d(TAG, "onSuccess: Url nabavljam");
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                String uploadedImageUrl = "" + uriTask.getResult();

                Log.d(TAG, "onSuccess: Uplodan image url " + uploadedImageUrl);
                updateProfile(uploadedImageUrl);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Neuspješan upload" + e.getMessage());
                Toast.makeText(EditProfilActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateProfile(String imageUrl) {
        Log.d(TAG, "updateProfile: Update user profile");
        progressDialog.setMessage("Ažuriranje profila");
        progressDialog.show();

        //setup data
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ime", ""+name);
        if(imageUri != null)
        {
            hashMap.put("profileImage", ""+ imageUrl);

        }
        //update data
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Profil ažuriran");
                        progressDialog.dismiss();
                        Toast.makeText(EditProfilActivity.this, "Profil ažuriran", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Neuspješno ažuriranje" + e.getMessage());
                        Toast.makeText(EditProfilActivity.this, "Neuspješno ažuriranje" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showImageAttachMenu() {
        //init/setup popup menu
        PopupMenu popupMenu = new PopupMenu(this, binding.profilePic);
        popupMenu.getMenu().add(Menu.NONE, 0 ,0, "Kamera");
        popupMenu.getMenu().add(Menu.NONE, 1 ,1, "Galerija");

        popupMenu.show();

        //handle menu item clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //get id of item clicked
                int which = item.getItemId();
                if(which == 0)
                {
                    //kamera
                    pickImageCamera();

                }
                else if (which==1)
                {
                    //galerija
                    pickImageGallery();
                }
                return false;
            }
        });
    }

    private void pickImageGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);

    }

    private void pickImageCamera() {
        //intent to pick image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Novi odabir");//naslov slike
        values.put(MediaStore.Images.Media.DESCRIPTION, "Opis");//opis
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);

    }
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //used to handle result of camera intent
                    //get uri of image
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Log.d(TAG, "onActivityResult: Picked from camera " + imageUri);
                        Intent data = result.getData(); //no need here as in camera case we already have image in imageUri variable
                        binding.profilePic.setImageURI(imageUri);
                    }
                    else
                    {
                        Toast.makeText(EditProfilActivity.this, "Poništeno", Toast.LENGTH_SHORT).show();
                    }


                }
            }

    );

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //used to handle result of gallery intent
                    //get uri of image
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Log.d(TAG, "onActivityResult: " + imageUri);
                        Intent data = result.getData();
                        imageUri = data.getData();

                        Log.d(TAG, "onActivityResult: Picked from gallery" + imageUri);
                        binding.profilePic.setImageURI(imageUri);
                    }
                    else
                    {
                        Toast.makeText(EditProfilActivity.this, "Poništeno", Toast.LENGTH_SHORT).show();
                    }


                }
            }

    );

    private void loadUserInfo() {
        Log.d(TAG, "loadUserInfo: Loading user info..." + firebaseAuth.getUid());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get all info of user here
                String email = ""+ snapshot.child("email").getValue();
                String name = ""+ snapshot.child("ime").getValue();
                String profileImage = ""+ snapshot.child("profileImage").getValue();
                String timestamp = ""+ snapshot.child("timestamp").getValue();
                String uid = ""+ snapshot.child("uid").getValue();
                String userType = ""+ snapshot.child("userType").getValue();

                //set data to UI
                binding.etIme.setText(name);

                //set image using glide
                Glide.with(EditProfilActivity.this).load(profileImage).placeholder(R.drawable.ic_baseline_person_24).into(binding.profilePic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}