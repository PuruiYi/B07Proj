package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.sport_events_scheduler.databinding.ActivityAdminBinding;

import com.google.firebase.database.DatabaseReference;


public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manageFragment(new EventFragment());

        Toast.makeText(getApplicationContext(), "Hi, " +
                getIntent().getStringExtra("user"), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "You are in <ADMIN> page", Toast.LENGTH_SHORT).show();

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

        Remote remote = new Remote();
        ref = remote.getEventRef();
    }

    private void manageFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}