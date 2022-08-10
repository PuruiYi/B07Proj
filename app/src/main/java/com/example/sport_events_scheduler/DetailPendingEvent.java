package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sport_events_scheduler.databinding.ActivityDetailPendingEventBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.lang.reflect.Parameter;
import java.util.HashMap;

public class DetailPendingEvent extends AppCompatActivity {

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


        Remote remote = new Remote();
        DatabaseReference ref = remote.getPendingEventRef();
        DatabaseReference eventRef = remote.getEventRef();
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
                Event modifiedEvent = getModifiedData();
                updateData(modifiedEvent);
                eventRef.child(modifiedEvent.getLocation()).child(modifiedEvent.getId()).setValue(modifiedEvent);
                ref.child(modifiedEvent.getId()).removeValue();
                Toast.makeText(DetailPendingEvent.this, "Accept event successfully", Toast.LENGTH_SHORT).show();

            }
        });

        binding.detailModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event modifiedEvent = getModifiedData();
                updateData(modifiedEvent);
            }
        });

    }

    private Event getModifiedData(){
        String m_venue = binding.detailVenue.getText().toString();
        String m_name = binding.detailName.getText().toString();
        int m_capacity = Integer.parseInt(binding.detailCapacity.getText().toString());
        String m_startTime = binding.detailStartTime.getText().toString();
        String m_endTime = binding.detailEndTime.getText().toString();
        return new Event(id,m_name,m_capacity,joined,m_startTime,m_endTime,m_venue);
    }

    private void updateData(Event modifiedEvent) {
        HashMap detailEventList = new HashMap();

        detailEventList.put("start",modifiedEvent.getStart());
        detailEventList.put("end",modifiedEvent.getEnd());
        detailEventList.put("name",modifiedEvent.getName());
        detailEventList.put("capacity",modifiedEvent.getCapacity());
        detailEventList.put("location",modifiedEvent.getLocation());

        Remote remote = new Remote();
        databaseReference = remote.getPendingEventRef();
        databaseReference.child(id).updateChildren(detailEventList).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    onBackPressed();
                    Toast.makeText(DetailPendingEvent.this,"pending event modified",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DetailPendingEvent.this,"failed to modify",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
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