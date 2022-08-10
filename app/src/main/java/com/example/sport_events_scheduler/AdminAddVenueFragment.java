package com.example.sport_events_scheduler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminAddVenueFragment extends Fragment {

    private Boolean tip;
    private View view;
    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private VenueAdapter adapter;
    private ArrayList<String> venues;
    private FloatingActionButton addVenueBtn;

    public AdminAddVenueFragment(Boolean tip) {
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
        view = inflater.inflate(R.layout.fragment_admin_add_venue, container, false);

        if (tip) {
            Toast.makeText(getActivity(), "All available venues listed here.", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Click + to add a new venue.", Toast.LENGTH_SHORT).show();
        }

        /** Initializer. */
        Remote remote = new Remote();
        ref = remote.getEventRef();
        addVenueBtn = view.findViewById(R.id.addVenue);
        recyclerView = view.findViewById(R.id.adminVenueList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        venues = new ArrayList<>();
        adapter = new VenueAdapter(this.getContext(), venues);
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

        addVenueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


        return view;
    }

    private void openDialog() {
        /** Initializer. */
        DialogFragment dialog = new AddVenueDialogFragment();
        dialog.show(getChildFragmentManager(), "AddVenueDialog");
    }
}