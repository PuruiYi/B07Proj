package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayActivitiesAtVenue extends AppCompatActivity {

    Remote remote;
    DatabaseReference ref;
    RecyclerView recyclerView;
    EventAdapter adapter;
    ArrayList<SportEvent> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_activities_at_avenue);

        Intent intent = getIntent();

        /** Initializer. */
        remote = new Remote();
        ref = remote.getAvenueRef(intent.getStringExtra("location"));
        recyclerView = findViewById(R.id.eventList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        events = new ArrayList<>();
        adapter = new EventAdapter(this, events);
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    //for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        SportEvent sportEvent = dataSnapshot.getValue(SportEvent.class);
                        events.add(sportEvent);
                    //}
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}