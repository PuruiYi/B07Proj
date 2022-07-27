package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

public class NewVenueActivity extends AppCompatActivity {

    Remote remote;
    EditText nameText, capacityText, locationText, startText, endText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_avenue);

        /** Initializer. */
        remote = new Remote();
        nameText = (EditText) findViewById(R.id.nameVenue);
        capacityText = (EditText) findViewById(R.id.capacityVenue);
        locationText = (EditText) findViewById(R.id.locationVenue);
        startText = (EditText) findViewById(R.id.startVenue);
        endText = (EditText) findViewById(R.id.endVenue);


    }

    public void add(View view) {
        String name = nameText.getText().toString();
        int capacity = Integer.parseInt(capacityText.getText().toString().trim());
        String location = locationText.getText().toString();
        String start = startText.getText().toString();
        String end = endText.getText().toString();

        SportEvent event = new SportEvent(name, capacity, 0, start, end, location);
        DatabaseReference ref = remote.getEventRef();
        ref.child(location).child(name).setValue(event);
    }
}