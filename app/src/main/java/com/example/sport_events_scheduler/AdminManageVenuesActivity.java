package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminManageVenuesActivity extends AppCompatActivity {

    Remote remote;
    Intent parent;
    DatabaseReference ref;
    RecyclerView recyclerView;
    AdminEventAdapter adapter;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_manage_avenue);

        /** Initializer. */
        remote = new Remote();
        parent = getIntent();
        ref = remote.getAvenueRef(parent.getStringExtra("location"));
        recyclerView = findViewById(R.id.adminEventList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        events = new ArrayList<>();
        adapter = new AdminEventAdapter(this, events);
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                events.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Event event = dataSnapshot.getValue(Event.class);
                    events.add(event);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageButton closeBtn = findViewById(R.id.adminCloseDisplayActivityBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void addNewActivity(View view) {
        DialogFragment dialog = new AddEventDialogFragment(parent.getStringExtra("location"));
        dialog.show(getSupportFragmentManager(), "AddEventDialog");
    }
}