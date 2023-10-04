package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.fitnessapp.AllForUsers.DashboardUserActivity;
import com.example.fitnessapp.databinding.ActivityMainBinding;
import com.example.fitnessapp.kategorije.CategoryFragment;
import com.example.fitnessapp.prijava.LoginActivity;
import com.example.fitnessapp.trening.TrListFragment;
import com.example.fitnessapp.trening.TrainingFragment;
import com.example.fitnessapp.upload.UploadCategoryFragment;
import com.example.fitnessapp.upload.UploadFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    //FirebaseAuth mAuth;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
       // mAuth = FirebaseAuth.getInstance();


         fragmentManager = getSupportFragmentManager();

        replaceFragment(new CategoryFragment());
        setContentView(binding.getRoot());

        binding.doleNavigacija.setBackground(null);
        binding.doleNavigacija.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    MyApplication.pocetna = true;
                    replaceFragment(new CategoryFragment());
                    break;
                case R.id.Training:
                    MyApplication.pocetna = false;
                    replaceFragment(new TrainingFragment());
                    break;
                case R.id.myTraining:
                    MyApplication.pocetna = false;
                    replaceFragment(new TrListFragment());
                    break;
            }

            return true;
        });

        binding.gumbek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();

            }
        });
        binding.logutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor  sharedEditor = sharedPreferences.edit();
                if(sharedPreferences.getBoolean("provjera", false))
                {
                    sharedEditor.clear();
                    sharedEditor.apply();
                }
                startActivity(new Intent(MainActivity.this, SplashActivity.class));



            }

        });

    }

    @Override
    public void onBackPressed() {
      if(MyApplication.pocetna != true)
      {
          MyApplication.pocetna = true;
          binding.doleNavigacija.setSelectedItemId(0);

          replaceFragment(new CategoryFragment());
      }
      else
      {
          super.onBackPressed();
      }

    }

    private  void replaceFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

       // if(getSupportFragmentManager().getBackStackEntryCount() !=1){
         //   fragmentTransaction.addToBackStack(null);
      //  }


    }

    private void showBottomDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new UploadCategoryFragment());
                MyApplication.pocetna = false;
                dialog.dismiss();
            }
        });
        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new UploadFragment());
                MyApplication.pocetna = false;
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}