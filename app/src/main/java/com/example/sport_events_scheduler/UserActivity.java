package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sport_events_scheduler.databinding.ActivityUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {

    ActivityUserBinding binding;
    DatabaseReference ref;
    Intent parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Remote remote = new Remote();
        ref = remote.getEventRef();
        parent = getIntent();
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manageFragment(new UserEventsFragment());

        Toast.makeText(getApplicationContext(), "Welcome, " +
                getIntent().getStringExtra("user") + " !", Toast.LENGTH_LONG).show();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.user_nav_events:
                    manageFragment(new UserEventsFragment());
                    break;

                case R.id.user_nav_upcomingEvents:
                    manageFragment(new UserUpcomingEventsFragment());
                    break;

                case R.id.user_nav_schedule:
                    manageFragment(new UserScheduleFragment());
                    break;
            }
            return true;
        });
    }

    private void manageFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void show_my_activities(View view) {
        Toast.makeText(this, "Your reservation will show up here", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), ShowMyActivities.class);
        startActivity(intent);
    }

    public void joinEvent(View view) {
        View layout = (View) view.getParent();
        String id = ((TextView) layout.findViewById(R.id.eventId)).getText().toString();

        Query e = ref.orderByKey().equalTo(id);
        e.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    /** If the user has already joined the event. */
                    for (DataSnapshot user : snapshot.child(id).child("users").getChildren()) {
                        if (user.getKey().equals(parent.getStringExtra("user"))) {

                            return;
                        }
                    }

                    int old = snapshot.child(id).child("joined").getValue(Integer.class);
                    int capacity = snapshot.child(id).child("capacity").getValue(Integer.class);
                    if (old != capacity) {
                        ref.child(id).child("joined").setValue(old + 1);
                        ref.child(id).child("users").child(parent.getStringExtra("user")).setValue("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    public void quitActivity(View view) {
        View layout = (View) view.getParent();
        String id = ((TextView) layout.findViewById(R.id.eventId)).getText().toString();

        Query event = ref.orderByKey().equalTo(id);
        event.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    /** If the user has already joined the event. */
                    for (DataSnapshot user : snapshot.child(id).child("users").getChildren()) {
                        if (user.getKey().equals(parent.getStringExtra("user"))) {
                            int old = snapshot.child(id).child("joined").getValue(Integer.class);
                            ref.child(id).child("joined").setValue(old - 1);
                            ref.child(id).child("users").child(parent.getStringExtra("user")).setValue("");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}