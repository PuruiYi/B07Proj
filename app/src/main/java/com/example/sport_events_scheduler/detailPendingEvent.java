package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sport_events_scheduler.databinding.ActivityAdmin2Binding;
import com.example.sport_events_scheduler.databinding.ActivityDetailPendingEventBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class detailPendingEvent extends AppCompatActivity {

    private Event detailedEvent;
    private String id, name, start, end, location;
    private int capacity, joined;

    ActivityDetailPendingEventBinding binding;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPendingEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Editing Pending Event");

        detailedEvent = getIntent().getParcelableExtra("DETAIL");
        id = detailedEvent.getId();
        name = detailedEvent.getName();
        capacity = detailedEvent.getCapacity(); //int
        start = detailedEvent.getStart();
        end = detailedEvent.getEnd();
        location = detailedEvent.getLocation();
        joined = detailedEvent.getJoined();  //int

        EditText venueView = findViewById(R.id.detailVenue);
        EditText nameView = findViewById(R.id.detailName);
        EditText capacityView = findViewById(R.id.detailCapacity);
        EditText startTimeView = findViewById(R.id.detailStartTime);
        EditText endTimeView = findViewById(R.id.detailEndTime);

        venueView.setText(location);
        nameView.setText(name);
        capacityView.setText(String.valueOf(capacity));
        startTimeView.setText(start);
        endTimeView.setText(end);
        binding.detailAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.detailModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m_venue = binding.detailVenue.getText().toString();
                String m_name = binding.detailName.getText().toString();
                int m_capacity = Integer.parseInt(binding.detailCapacity.getText().toString());
                String m_startTime = binding.detailStartTime.getText().toString();
                String m_endTime = binding.detailEndTime.getText().toString();

                updateData(m_name,m_capacity,m_startTime,m_endTime,m_venue);
            }
        });

    }

    private void updateData(String m_name, int m_capacity, String m_startTime, String m_endTime, String m_venue) {
        HashMap detailEventList = new HashMap();

        detailEventList.put("start",m_startTime);
        detailEventList.put("end",m_endTime);
        detailEventList.put("name",m_name);
        detailEventList.put("capacity",m_capacity);
        detailEventList.put("location",m_venue);

        Remote remote = new Remote();
        databaseReference = remote.getPendingEventRef();
        databaseReference.child(id).updateChildren(detailEventList).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    binding.detailVenue.setText(m_venue);
                    binding.detailName.setText(m_name);
                    binding.detailCapacity.setText(String.valueOf(m_capacity));
                    binding.detailStartTime.setText(m_startTime);
                    binding.detailEndTime.setText(m_endTime);
                    Toast.makeText(detailPendingEvent.this,"pending event modified",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(detailPendingEvent.this,"failed to modify",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //TODO: back to fragment, not activity
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.adminActivity2) {
//            Intent parentIntent = NavUtils.getParentActivityIntent(this);
//            parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(parentIntent);
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}