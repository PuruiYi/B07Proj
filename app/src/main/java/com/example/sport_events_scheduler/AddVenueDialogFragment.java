package com.example.sport_events_scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddVenueDialogFragment extends DialogFragment {

    private DatabaseReference eventRef;
    private EditText venueNameText;
    private Button addBtn;
    private ImageButton closeBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.DialogActivity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_venue, null);
        // Components.
        venueNameText = view.findViewById(R.id.addVenueNameText);
        addBtn = view.findViewById(R.id.addVenueBtn);
        closeBtn = view.findViewById(R.id.closeBtn);
        // DatabaseReference
        eventRef = new Remote().getEventRef();
        // Builder setup.
        builder.setView(view);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (addNewVenue()) {
                   dismiss();
                   Toast.makeText(getActivity(), "New Venue is added.", Toast.LENGTH_LONG).show();
               }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    private boolean addNewVenue() {
        String name = venueNameText.getText().toString();

        if (!isValidName(venueNameText, name))
            return false;

        /** Clear errors. */
        venueNameText.setError(null);

        Query checkVenue = eventRef.orderByKey().equalTo(name);
        checkVenue.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    venueNameText.setError("Venue provided exists");
                    venueNameText.requestFocus();
                }
                else {
                    venueNameText.setError(null);
                    eventRef.child(name).setValue("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        venueNameText.setText("");

        return venueNameText.getError() == null ? true : false;
    }

    /** Validate venue name provided by admins. */
    private boolean isValidName(EditText nameText, String name) {
        Pattern pattern = Pattern.compile("\\s*");
        Matcher usernameM = pattern.matcher(name);
        if (usernameM.matches()) {
            nameText.setError("Name cannot be empty.");
            nameText.requestFocus();
            return false;
        }
        return true;
    }

}
