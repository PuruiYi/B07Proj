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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingEventFragment extends Fragment implements PendingEventAdapter.OnItemClickListener {

    private boolean tip;
    private DatabaseReference ref;  //ref to pendingEvent
    private DatabaseReference eventRef; //ref to event
    private RecyclerView recyclerView;
    private PendingEventAdapter pendingEventAdapter;
    private ArrayList<Event> pendingEvents;

    public PendingEventFragment(Boolean tip) {
        super();
        this.tip = tip;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_event, container, false);
        getActivity().setTitle("View Pending Events");

        // First time tip.
        if (tip) {
            Toast.makeText(getActivity(), "All pending events listed here.", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Click to modify.", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Accept/reject by clicking on the top-right conor", Toast.LENGTH_SHORT).show();
        }

        recyclerView = view.findViewById(R.id.pendingEventList);
        Remote remote = new Remote();
        ref = remote.getPendingEventRef();
        eventRef = remote.getEventRef();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        pendingEvents = new ArrayList<>();
        pendingEventAdapter = new PendingEventAdapter(this.getContext(),pendingEvents,this);
        recyclerView.setAdapter(pendingEventAdapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingEvents.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Event pendingEvent = dataSnapshot.getValue(Event.class);

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