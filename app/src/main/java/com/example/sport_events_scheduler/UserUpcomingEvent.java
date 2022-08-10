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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class UserUpcomingEvent extends AppCompatActivity {

    Remote remote;
    Intent parent;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    EventAdapter adapter;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_upcoming_events);


        remote = new Remote();
        parent = getIntent();
        databaseReference = remote.getEventRef();
        recyclerView = findViewById(R.id.eventList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        events = new ArrayList<>();
        adapter = new EventAdapter(this, events);
        recyclerView.setAdapter(adapter);
//code to get all children events from venues
        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Event event = ds.getValue(Event.class);
                                events.add(event);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

//code to find events ends
    }


    /** i still have to add code to store the id of user who joined the event*/
    public void joinEvent(View view) {
        View layout = (View) view.getParent();
        String id = ((TextView)layout.findViewById(R.id.eventId)).getText().toString();

        Query e = databaseReference.orderByKey().equalTo(id);
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
                        databaseReference.child(id).child("joined").setValue(old + 1);
                        databaseReference.child(id).child("users").child(parent.getStringExtra("user")).setValue("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

}