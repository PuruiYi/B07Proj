package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayActivitiesAtVenue extends AppCompatActivity {

    Remote remote;
    Intent parent;
    DatabaseReference ref;
    RecyclerView recyclerView;
    EventAdapter adapter;
    ArrayList<SportEvent> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_activities_at_avenue);


        /** Initializer. */
        remote = new Remote();
        parent = getIntent();
        ref = remote.getAvenueRef(parent.getStringExtra("location"));
        recyclerView = findViewById(R.id.eventList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        events = new ArrayList<>();
        adapter = new EventAdapter(this, events);
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                events.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    SportEvent sportEvent = dataSnapshot.getValue(SportEvent.class);
                    events.add(sportEvent);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void addNewActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), AddNewEvent.class);
        intent.putExtra("location", parent.getStringExtra("location"));
        startActivity(intent);
    }

}