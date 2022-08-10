package com.example.sport_events_scheduler;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserUpcomingEventsFragment extends Fragment {

    View view;
    DatabaseReference ref;
    RecyclerView recyclerView;
    EventAdapter adapter;
    ArrayList<Event> events;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** Inflate the layout for this fragment. */
        view = inflater.inflate(R.layout.fragment_user_upcoming_events, container, false);

        /** Initializer. */
        Remote remote = new Remote();
        ref = remote.getEventRef();
        recyclerView = view.findViewById(R.id.eventList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        events = new ArrayList<>();
        adapter = new EventAdapter(this.getContext(), events);
        recyclerView.setAdapter(adapter);
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        events.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                System.out.println(ds.getValue());
                                Event event = ds.getValue(Event.class);
                                events.add(event);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        return view;
    }

    /** i still have to add code to store the id of user who joined the event*/
    public void joinEvent(View view) {
        View layout = (View) view.getParent();
        String id = ((TextView)layout.findViewById(R.id.eventId)).getText().toString();

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
}