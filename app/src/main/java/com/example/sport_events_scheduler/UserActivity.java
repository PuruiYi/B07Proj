package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sport_events_scheduler.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {

    private Boolean upcomingTip, eventsTip, scheduleTip;
    private ActivityUserBinding binding;
    private String currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        upcomingTip = true; eventsTip = true; scheduleTip = true;

        currUser = getIntent().getStringExtra("user");

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manageFragment(new UserUpcomingEventsFragment());

        Toast.makeText(getApplicationContext(), "Hi, " +
                getIntent().getStringExtra("user"), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "You are in <USER> page", Toast.LENGTH_SHORT).show();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.user_nav_upcomingEvents:
                    manageFragment(new UserUpcomingEventsFragment());
                    upcomingTip = false;
                    break;

                case R.id.user_nav_events:
                    manageFragment(new UserEventsFragment(eventsTip));
                    eventsTip = false;
                    break;

                case R.id.user_nav_schedule:
                    manageFragment(new UserScheduleFragment(currUser, scheduleTip));
                    scheduleTip = false;
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