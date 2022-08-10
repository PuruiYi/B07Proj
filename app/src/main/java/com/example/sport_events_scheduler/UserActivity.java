package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sport_events_scheduler.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {

    ActivityUserBinding binding;
    String currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currUser = getIntent().getStringExtra("user");

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manageFragment(new UserEventsFragment());

        Toast.makeText(getApplicationContext(), "Hi, " +
                getIntent().getStringExtra("user"), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "You are in <USER> page", Toast.LENGTH_SHORT).show();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.user_nav_upcomingEvents:
                    manageFragment(new UserUpcomingEventsFragment());
                    break;

                case R.id.user_nav_events:
                    manageFragment(new UserEventsFragment());
                    break;

                case R.id.user_nav_schedule:

//                    Bundle bundle = new Bundle();
//                    bundle.putString("user", currUser);
//                    UserScheduleFragment fragment = new UserScheduleFragment(currUser);
//                    fragment.setArguments(bundle);


                    manageFragment(new UserScheduleFragment(currUser));
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