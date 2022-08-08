package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sport_events_scheduler.databinding.ActivityMainBinding;
import com.example.sport_events_scheduler.databinding.ActivityUser2Binding;

public class UserActivity2 extends AppCompatActivity {

    ActivityUser2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUser2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manageFragment(new UserEventsFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.user_nav_events:
                    manageFragment(new UserEventsFragment());
                    break;

                case R.id.user_nav_schedule:
                    manageFragment(new UserScheduleFragment());
                    break;
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