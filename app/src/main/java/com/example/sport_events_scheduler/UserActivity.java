package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    DatabaseReference ref;
    RecyclerView recyclerView;
    VenueAdapter adapter;
    ArrayList<String> venues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        /** Welcome message. */
        Toast.makeText(getApplicationContext(), "Welcome, " +
                        getIntent().getStringExtra("user") + " !", Toast.LENGTH_LONG).show();

        /** Initializer. */
        Remote remote = new Remote();
        ref = remote.getEventRef();
        recyclerView = findViewById(R.id.avenueList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        venues = new ArrayList<>();
        adapter = new VenueAdapter(this, venues);
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                venues.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String venue = dataSnapshot.getKey();
                    venues.add(venue);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void showActivities(View view) {

        Intent intent = new Intent(getApplicationContext(), DisplayActivitiesAtVenue.class);
        TextView location = (TextView) view.findViewById(R.id.nameAvenueLabel);
        intent.putExtra("location", location.getText().toString());
        startActivity(intent);

    }
}