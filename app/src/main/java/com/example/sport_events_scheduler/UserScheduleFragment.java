package com.example.sport_events_scheduler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserScheduleFragment extends Fragment {

    View view;
    DatabaseReference accontRef;
    DatabaseReference eventRef;

    ListView lvItems;
    String currUser;

    public UserScheduleFragment(String currUser) {
        super();
        this.currUser = currUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_schedule, container, false);
        lvItems = (ListView) view.findViewById(R.id.lvItems);

        ArrayList<Event> events = new ArrayList<Event>();
        ScheduleItemAdapter adapter = new ScheduleItemAdapter(this.getContext(), R.layout.my_schedule_layout, events);
        lvItems.setAdapter(adapter);

        Remote remote = new Remote();
        accontRef = remote.getAccountRef();
        eventRef = remote.getEventRef();

        accontRef.child(currUser).child("joined").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String event_id = dataSnapshot.getKey();
                    String venue = dataSnapshot.getValue(String.class);
                    eventRef.child(venue).child(event_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Event evt = snapshot.getValue(Event.class);
                            // Toast.makeText(getActivity(), evt.getName(), Toast.LENGTH_LONG).show();
                            events.add(evt);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return view;
    }
}