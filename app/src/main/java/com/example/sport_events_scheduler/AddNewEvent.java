package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

public class AddNewEvent extends AppCompatActivity {

    Remote remote;
    Intent parent;
    EditText nameText, capacityText, startText, endText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        /** Initializer. */
        remote = new Remote();
        parent = getIntent();
        nameText = (EditText) this.findViewById(R.id.nameVenue);
        capacityText = (EditText) this.findViewById(R.id.capacityVenue);
        startText = (EditText) this.findViewById(R.id.startVenue);
        endText = (EditText) this.findViewById(R.id.endVenue);
    }

    public void add(View view) {

        String name = nameText.getText().toString();
        int capacity = Integer.parseInt(capacityText.getText().toString().trim());
        String location = parent.getStringExtra("location");
        String start = startText.getText().toString();
        String end = endText.getText().toString();

        SportEvent event = new SportEvent(name, capacity, 0, start, end, location);
        DatabaseReference ref = remote.getEventRef();
        ref.child(location).child(name).setValue(event);

    }
}