package com.example.sport_events_scheduler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingEventFragment extends Fragment implements PendingEventAdapter.OnItemClickListener {

    DatabaseReference ref;  //ref to pendingEvent
    DatabaseReference eventRef; //ref to event
    RecyclerView recyclerView;
    PendingEventAdapter pendingEventAdapter;
    ArrayList<Event> pendingEvents;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_event, container, false);

        recyclerView = view.findViewById(R.id.pendingEventList);
        Remote remote = new Remote();
        ref = remote.getPendingEventRef();
        eventRef = remote.getEventRef();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        pendingEvents = new ArrayList<>();
        pendingEventAdapter = new PendingEventAdapter(this.getContext(),pendingEvents,this);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingEvents.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Event pendingEvent = dataSnapshot.getValue(Event.class);
                        recyclerView.setAdapter(pendingEventAdapter);
                        pendingEvents.add(pendingEvent);

                }
                pendingEventAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void onItemClick(Event pendingEvent, int state) {
        if (state == 1){
            eventRef.child(pendingEvent.getLocation()).child(pendingEvent.getId()).setValue(pendingEvent);
            ref.child(pendingEvent.getId()).removeValue();
            Toast.makeText(getActivity(), "Accept event successfully", Toast.LENGTH_SHORT).show();
        }else if(state == 2){
            ref.child(pendingEvent.getId()).removeValue();
            Toast.makeText(getActivity(), "Reject event successfully", Toast.LENGTH_SHORT).show();
        }else{}
    }
}