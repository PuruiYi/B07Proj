package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditEventActivity extends AppCompatActivity implements EventAdapter.EventOnClickListener{
    Remote remote;
    Intent intent;
    DatabaseReference ref;
    RecyclerView recyclerView;
    EventAdapter adapter;
    ArrayList<Event> events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        /** Initializer. */
        remote = new Remote();
        intent = getIntent();
        ref = remote.getAvenueRef(intent.getStringExtra("location"));
        recyclerView = findViewById(R.id.adminEventList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        events = new ArrayList<>();
        adapter = new EventAdapter(this, events, this);
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                events.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Date now = Calendar.getInstance().getTime();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    Time localtime = new Time(format.format(now));

                    Event event = dataSnapshot.getValue(Event.class);
                    Time start_time = new Time(event.getStart());

                    if(start_time.compareTo(localtime) > 0)
                        events.add(event);


                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void displayActivity(View view) {
        Intent intent = new Intent(this, EditEvent2Activity.class);
        TextView event_id = view.findViewById(R.id.eventId);
        TextView event_name = view.findViewById(R.id.eventName);
        TextView event_capacity = view.findViewById(R.id.eventCapacity);
        TextView event_time = view.findViewById(R.id.eventTime);
        TextView event_joined = view.findViewById(R.id.eventJoined);
        TextView event_location = view.findViewById(R.id.eventLocation);

        Pattern pattern = Pattern.compile("(\\d{1,2}:\\d\\d)\\s+-\\s+(\\d{1,2}:\\d\\d)");
        Matcher matcher = pattern.matcher(event_time.getText().toString());
        matcher.find();
        intent.putExtra("id", event_id.getText().toString());
        intent.putExtra("name", event_name.getText().toString());
        intent.putExtra("capacity", event_capacity.getText().toString());
        intent.putExtra("joined", event_joined.getText().toString());
        intent.putExtra("start", matcher.group(1));
        intent.putExtra("end", matcher.group(2));
        intent.putExtra("location", event_location.getText().toString());
        startActivity(intent);
    }


}