package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.EditText;

import com.example.sport_events_scheduler.databinding.ActivityAdmin2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminActivity extends AppCompatActivity {

    ActivityAdmin2Binding binding;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
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