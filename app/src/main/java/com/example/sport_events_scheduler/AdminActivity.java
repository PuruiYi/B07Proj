package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.sport_events_scheduler.databinding.ActivityAdmin2Binding;

public class AdminActivity extends AppCompatActivity {

    ActivityAdmin2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdmin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manageFragment(new EventFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.event){
                manageFragment(new EventFragment());
            }
            if(item.getItemId() == R.id.pendingEvent){
                manageFragment(new PendingEventFragment());
            }
            if(item.getItemId() == R.id.account){
                manageFragment(new AdminAddVenueFragment());
            }
            return true;
        });
    }

    private void manageFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}