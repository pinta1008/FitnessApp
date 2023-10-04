package com.example.fitnessapp.profil;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.fitnessapp.MyApplication;
import com.example.fitnessapp.R;
import com.example.fitnessapp.databinding.FragmentProfileBinding;
import com.example.fitnessapp.profil.EditProfilActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "PROFILE_TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(getLayoutInflater());


        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();
        //handle click


        binding.editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfilActivity.class));
            }
        });

        return binding.getRoot();


    }

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

                String formattedDate = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                if(userType.equals("user"))
                {
                    binding.accountTypeTv.setText("Korisnik");
                }
                else
                {
                    binding.accountTypeTv.setText("Administrator");
                }

                //set data to UI
                binding.emailTv.setText(email);
                binding.nameTv.setText(name);
                binding.memberDateTv.setText(formattedDate);
               // binding.accountTypeTv.setText(userType);

                //set image using glide
                Glide.with(getActivity()).load(profileImage).placeholder(R.drawable.uploadimg).into(binding.profileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}