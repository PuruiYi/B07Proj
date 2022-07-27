package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddNewVenue extends AppCompatActivity {

    Remote remote;
    EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_avenue);

        /** Initializer. */
        remote = new Remote();
        nameText = (EditText) this.findViewById(R.id.nameVenue);

    }

    public void addNewVenue(View view) {
        String name = nameText.getText().toString();

        DatabaseReference ref = remote.getEventRef();
        Query checkVenue = ref.orderByKey().equalTo(name);
        checkVenue.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    nameText.setError("Venue provided exists");
                    nameText.requestFocus();
                }
                else {
                    nameText.setError(null);
                    ref.child(name).setValue("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}