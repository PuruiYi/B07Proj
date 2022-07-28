package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
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
        ref = remote.getVenueRef(parent.getStringExtra("location"));
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

    public void joinActivity(View view) {
        View layout = (View) view.getParent();
        String id = ((TextView)layout.findViewById(R.id.eventId)).getText().toString();

        Query event = ref.orderByKey().equalTo(id);
        event.addListenerForSingleValueEvent(new ValueEventListener() {
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

            }
        });
    }

}