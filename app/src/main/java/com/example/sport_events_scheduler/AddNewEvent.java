package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewEvent extends AppCompatActivity {

    Remote remote;
    Intent parent;
    EditText nameText, capacityText, startText, endText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        /** Set display window size. */

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.9));

        /** Initializer. */
        remote = new Remote();
        parent = getIntent();
        nameText = (EditText) this.findViewById(R.id.nameVenue);
        capacityText = (EditText) this.findViewById(R.id.capacityVenue);
        startText = (EditText) this.findViewById(R.id.startVenue);
        endText = (EditText) this.findViewById(R.id.endVenue);
    }

    public void add(View view) {

        /** Event info. */
        String name = nameText.getText().toString();
        String capacity = capacityText.getText().toString();
        String location = parent.getStringExtra("location");
        String start = startText.getText().toString();
        String end = endText.getText().toString();
        /** Validate Inputs. */
        if (!isValidEvent(name, capacity, start, end))
            return;
        /** Clear errors. */
        nameText.setError(null);
        capacityText.setError(null);
        startText.setError(null);
        endText.setError(null);
        /** Reference to the path. */
        DatabaseReference ref = remote.getEventRef().child(location).push();
        String id = ref.getKey();
        /** SportEvent Object. */
        Event event = new Event(id, name, Integer.parseInt(capacity), 0, start, end, location);
        /** Push data into the database. */
        ref.setValue(event);
        /** Clear Inputs. */
        nameText.setText("");
        capacityText.setText("");
        startText.setText("");
        endText.setText("");
    }

    public void eventRequest(View view) {

        /** Event info. */
        String name = nameText.getText().toString();
        String capacity = capacityText.getText().toString();
        String location = parent.getStringExtra("location");
        String start = startText.getText().toString();
        String end = endText.getText().toString();
        /** Validate Inputs. */
        if (!isValidEvent(name, capacity, start, end))
            return;
        /** Clear errors. */
        nameText.setError(null);
        capacityText.setError(null);
        startText.setError(null);
        endText.setError(null);
        /** Reference to the path. */
        DatabaseReference ref = remote.getPendingEventRef().push();
        String id = ref.getKey();
        /** SportEvent Object. */
        Event event = new Event(id, name, Integer.parseInt(capacity), 0, start, end, location);
        /** Push data into the database. */
        ref.setValue(event);
        /** Clear Inputs. */
        nameText.setText("");
        capacityText.setText("");
        startText.setText("");
        endText.setText("");
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