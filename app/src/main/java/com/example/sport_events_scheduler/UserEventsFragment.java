package com.example.sport_events_scheduler;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserEventsFragment extends Fragment implements VenueAdapter.VenueOnClickListener {

    View view;
    DatabaseReference ref;
    RecyclerView recyclerView;
    VenueAdapter adapter;
    ArrayList<String> venues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** Inflate the layout for this fragment. */
        view = inflater.inflate(R.layout.fragment_user_events, container, false);

        /** Initializer. */
        Remote remote = new Remote();
        ref = remote.getEventRef();
        recyclerView = view.findViewById(R.id.avenueList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        venues = new ArrayList<>();
        adapter = new VenueAdapter(this.getContext(), venues, this);
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                venues.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String venue = dataSnapshot.getKey();
                    venues.add(venue);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void displayActivity(View view) {
        Intent intent = new Intent(this.getActivity(), DisplayActivitiesAtVenue.class);
        TextView location = view.findViewById(R.id.nameAvenueLabel);
        intent.putExtra("location", location.getText().toString());
        startActivity(intent);
    }
}