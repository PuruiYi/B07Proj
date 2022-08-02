package com.example.sport_events_scheduler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingEventFragment extends Fragment {

    DatabaseReference ref;
    RecyclerView recyclerView;
    PendingEventAdapter pendingEventAdapter;
    ArrayList<PendingEvent> pendingEventArrayList;

    public Task<Void> add(PendingEvent p){
        return ref.push().setValue(p);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashmap){
        return ref.child(key).updateChildren(hashmap);
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

        recyclerView = view.findViewById(R.id.pendingEventList);
        Remote remote = new Remote();
        ref = remote.getPendingEventRef();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        pendingEventArrayList = new ArrayList<>();
        pendingEventAdapter = new PendingEventAdapter(this.getContext(),pendingEventArrayList);
//        recyclerView.setAdapter(pendingEventAdapter);

        //get pendingEvent, add it to event
//        PendingEvent pe = (PendingEvent)getActivity().getIntent().getSerializableExtra("EVENT");
//        if(pe != null){
//            SportEvent se = new SportEvent(pe.getName(), pe.getCapacity(), 0, pe.getStart(), pe.getEnd(), pe.getLocation());
//
//        }



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingEventArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
                        PendingEvent pendingEvent = ds.getValue(PendingEvent.class);
                        recyclerView.setAdapter(pendingEventAdapter);
                        pendingEventArrayList.add(pendingEvent);

                    }
                }
                pendingEventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}