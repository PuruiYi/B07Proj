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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingEventFragment extends Fragment implements PendingEventAdapter.OnItemClickListener {

    DatabaseReference ref;  //ref to pendingEvent
    DatabaseReference eventRef; //ref to event
    RecyclerView recyclerView;
    PendingEventAdapter pendingEventAdapter;
    ArrayList<PendingEvent> pendingEventArrayList;

    public Task<Void> add(PendingEvent p){
//        String key = ref.push().getKey();
        //TODO:set key for event
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
        eventRef = remote.getEventRef();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        pendingEventArrayList = new ArrayList<>();
        pendingEventAdapter = new PendingEventAdapter(this.getContext(),pendingEventArrayList,this);
        recyclerView.setAdapter(pendingEventAdapter);

        //TODO:get pendingEvent, add it to event
//        PendingEvent pe = (PendingEvent)getActivity().getIntent().getSerializableExtra("EVENT");
//        SportEvent se = new SportEvent(pe.getName(), pe.getCapacity(), 0, pe.getStart(), pe.getEnd(), pe.getLocation());
//        eventRef.push().setValue(se);

//        String test = getActivity().getIntent().getStringExtra("TEST");
//        Toast.makeText(getActivity(),"test",Toast.LENGTH_LONG).show();


//        Toast.makeText(getActivity(),"hello from fragment",Toast.LENGTH_LONG).show();
//        PendingEvent pe = (PendingEvent) getArguments().getParcelable("EVENT");
//        String test = getArguments().getString("EVENT");
//        Toast.makeText(getActivity(),test,Toast.LENGTH_LONG).show();
//        SportEvent se = new SportEvent(pe.getName(), pe.getCapacity(), 0, pe.getStart(), pe.getEnd(), pe.getLocation());
//        eventRef.push().setValue(se);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingEventArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        PendingEvent pendingEvent = dataSnapshot.getValue(PendingEvent.class);
                        recyclerView.setAdapter(pendingEventAdapter);
                        pendingEventArrayList.add(pendingEvent);
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
    public void onItemClick(PendingEvent pendingEvent, int state) {
        if (state == 1){
            eventRef.child(pendingEvent.getLocation()).push().setValue(pendingEvent);
            ref.child(pendingEvent.getId()).removeValue();
            Toast.makeText(getActivity(), "Accept event successfully", Toast.LENGTH_SHORT).show();
        }else if(state == 2){
            ref.child(pendingEvent.getId()).removeValue();
            Toast.makeText(getActivity(), "Reject event successfully", Toast.LENGTH_SHORT).show();
        }else{}
    }
}