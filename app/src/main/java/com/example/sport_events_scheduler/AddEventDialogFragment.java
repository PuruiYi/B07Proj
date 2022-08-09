package com.example.sport_events_scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEventDialogFragment extends DialogFragment {

    private Remote remote;
    private String venue;
    private EditText nameText, capacityText, startText, endText;
    private Button addBtn;
    private ImageButton closeBtn;

    public AddEventDialogFragment(String venue) {
        this.venue = venue;
    }

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
        View view = inflater.inflate(R.layout.add_event, null);
        // Components.
        remote = new Remote();
        nameText = view.findViewById(R.id.nameVenue);
        capacityText = view.findViewById(R.id.capacityVenue);
        startText = view.findViewById(R.id.startVenue);
        endText = view.findViewById(R.id.endVenue);
        addBtn = view.findViewById(R.id.addButton);

        // Builder setup.
        builder.setView(view);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (eventRequest())
                   dismiss();
            }
        });
        /*
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });*/

        return builder.create();
    }

    private boolean eventRequest() {

        /** Event info. */
        String name = nameText.getText().toString();
        String capacity = capacityText.getText().toString();
        String start = startText.getText().toString();
        String end = endText.getText().toString();
        /** Validate Inputs. */
        if (!isValidEvent(name, capacity, start, end))
            return false;
        /** Clear errors. */
        nameText.setError(null);
        capacityText.setError(null);
        startText.setError(null);
        endText.setError(null);
        /** Reference to the path. */
        DatabaseReference ref = remote.getPendingEventRef().push();
        String id = ref.getKey();
        /** SportEvent Object. */
        Event event = new Event(id, name, Integer.parseInt(capacity), 0, start, end, venue);
        /** Push data into the database. */
        ref.setValue(event);
        /** Clear Inputs. */
        nameText.setText("");
        capacityText.setText("");
        startText.setText("");
        endText.setText("");
        return true;
    }

    /** Validate event information provided by users. */
    private boolean isValidEvent(String name, String capacity, String start, String end) {
        Pattern pattern = Pattern.compile("\\s*");
        Pattern time = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9]");
        Matcher nameM = pattern.matcher(name);
        Matcher capacityM = pattern.matcher(capacity);
        Matcher startM = time.matcher(start);
        Matcher endM = time.matcher(end);
        if (nameM.matches()) {
            nameText.setError("Name cannot be empty.");
            nameText.requestFocus();
            return false;
        }
        if (capacityM.matches() || Integer.parseInt(capacity) <= 0) {
            capacityText.setError("Capacity should be > 0.");
            capacityText.requestFocus();
            return false;
        }
        if (!startM.matches()) {
            startText.setError("Time should be in 24-hour format.");
            startText.requestFocus();
            return false;
        }
        if (!endM.matches()) {
            endText.setError("Time should be in 24-hour format.");
            endText.requestFocus();
            return false;
        }
        return true;
    }

}
