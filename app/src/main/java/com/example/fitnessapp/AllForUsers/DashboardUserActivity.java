package com.example.fitnessapp.AllForUsers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.fitnessapp.MyApplication;
import com.example.fitnessapp.R;
import com.example.fitnessapp.SplashActivity;
import com.example.fitnessapp.trening.TrListFragment;
import com.example.fitnessapp.trening.TrainingFragment;
import com.example.fitnessapp.databinding.ActivityDashboardUserBinding;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardUserActivity extends AppCompatActivity {

    ActivityDashboardUserBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new UserCategoryFragment());

        binding.doleNavigacija.setBackground(null);
        binding.doleNavigacija.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    MyApplication.pocetna = true;
                    replaceFragment(new UserCategoryFragment());
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
                // showBottomDialog();

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
                    sharedEditor.commit();
                }
                startActivity(new Intent(DashboardUserActivity.this, SplashActivity.class));


            }
        });

       // binding.profileBtn.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View view) {
              //  ProfileFragment profileFragment = new ProfileFragment();
              //  UserCategoryFragment userCategoryFragment = new UserCategoryFragment();

               // DashboardUserActivity.this.getSupportFragmentManager().beginTransaction().hide(userCategoryFragment)
                   //     .replace(R.id.layout_rel, profileFragment, null)
                   //     .setReorderingAllowed(true)
                    //    .addToBackStack(null).commit();
          //  }
      //  });

    }


    public void onBackPressed() {
        if(MyApplication.pocetna != true)
        {
            MyApplication.pocetna = true;
            binding.doleNavigacija.setSelectedItemId(0);

            replaceFragment(new UserCategoryFragment());
        }
        else
        {
            super.onBackPressed();
        }

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }



}


