package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class EditEvent2Activity extends AppCompatActivity {
    String name;
    int capacity;
    int joined;
    String location;
    String start;
    String end;
    String id;
    ArrayList<String> usernames;
    EditText Name;
    EditText Capacity;
    EditText Joined;
    EditText Location;
    EditText Start;
    EditText End;
    EditText Id;
    TextView Text;
    Button Modify;
    Button Delete;
    LinearLayout Linear;
    Remote remote;
    DatabaseReference rf;
    ArrayList<String> users;
    ArrayList<String> Location_List;
    boolean success;

    static synchronized void sync(CountDownLatch done){
        done.countDown();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event2);

        Intent intent = getIntent();

        this.name = intent.getStringExtra("name");
        this.capacity = intent.getIntExtra("capacity", 0);
        this.joined = intent.getIntExtra("joined", 0);
        this.location = intent.getStringExtra("location");
        this.start = intent.getStringExtra("start");
        this.end = intent.getStringExtra("end");
        this.id = intent.getStringExtra("id");
        this.usernames = intent.getStringArrayListExtra("usernames");

        this.Name = (EditText)findViewById(R.id.eventName);
        this.Capacity = (EditText)findViewById(R.id.eventCapacity);
        this.Joined = (EditText)findViewById(R.id.eventJoined);
        this.Location = (EditText)findViewById(R.id.eventLocation);
        this.Start = (EditText)findViewById(R.id.eventStartAt);
        this.End = (EditText)findViewById(R.id.eventEndAt);
        this.Id = (EditText)findViewById(R.id.eventId);
        this.Text = (TextView)findViewById(R.id.edittext4);

        Linear = (LinearLayout)findViewById(R.id.linearLayout);

        this.Name.setText(this.name);
        this.Capacity.setText(String.valueOf(this.capacity));
        this.Joined.setText(String.valueOf(this.joined));
        this.Location.setText(this.location);
        this.Start.setText(this.start);
        this.End.setText(this.end);
        this.Id.setText(this.id);

        NonEditable(this.Name);
        NonEditable(this.Capacity);
        NonEditable(this.Joined);
        NonEditable(this.Location);
        NonEditable(this.Start);
        NonEditable(this.End);
        NonEditable(this.Id);

        this.Modify = (Button)findViewById(R.id.button1);
        this.Delete = (Button)findViewById(R.id.button2);

        users = new ArrayList<String>();
        Location_List = new ArrayList<String>();

        remote = new Remote();
        rf = remote.getAvenueRef(this.location);
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    rf.removeEventListener(this);
                    return;
                }
                if(!snapshot.hasChild(id))
                    return;
                if(!snapshot.child(id).hasChild("joined"))
                    return;
                Joined.setText(snapshot.child(id).child("joined").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    rf.removeEventListener(this);
                    return;
                }
                if(!snapshot.hasChild(id))
                    return;
                if(!snapshot.child(id).hasChild("users"))
                    return;
                users.clear();
                for(DataSnapshot datasnapshot: snapshot.child(id).child("users").getChildren()){
                    users.add(datasnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rf = remote.getEventRef();
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Location_List.clear();
                for(DataSnapshot datasnapshot: snapshot.getChildren()){
                    Location_List.add(datasnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void Editable(View view){
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.requestFocus();
    }

    void NonEditable(View view){
        view.setFocusable(false);
        view.setFocusableInTouchMode(false);
    }

    void Delete(String id1){
        rf = remote.getAccountRef();

        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChildren())
                    return;
                for(DataSnapshot datasnapshot: snapshot.getChildren()){
                    if(!datasnapshot.hasChild("joined"))
                        continue;
                    if(!datasnapshot.child("joined").hasChild(id1))
                        continue;
                    datasnapshot.child("joined").child(id1).getRef().removeValue();
                }
                rf.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rf = remote.getAvenueRef(location);

        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(id1))
                    return;
                if(snapshot.getChildrenCount() == 1){
                    DatabaseReference rf1 = remote.getEventRef();
                    String location1 = snapshot.getKey();
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put(location1, "");
                    snapshot.child(id1).getRef().removeValue();
                    rf1.updateChildren(map);
                }
                else{
                    snapshot.child(id1).getRef().removeValue();
                }
                rf.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void Delete_Event(View view){
        String id1 = new String(this.id);
        Delete(id1);
        Linear.removeViews(0, 8);
        this.Text.setText("Delete success");
        this.Text.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    public void Modify_Event(View view){
        if(this.Modify.getText().toString().equals("Modify")){
            Editable(this.Capacity);
            Editable(this.Name);
            Editable(this.Start);
            Editable(this.End);
            Editable(this.Location);
            this.Modify.setText("Submit");
            this.Delete.setVisibility(View.INVISIBLE);
            this.Text.setVisibility(View.INVISIBLE);
            return;
        }

        if(this.Name.getText().toString().isEmpty()){
            this.Text.setText("Name can not be empty");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(this.Location.getText().toString().isEmpty()){
            this.Text.setText("Location can not be empty");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(this.Start.getText().toString().isEmpty()){
            this.Text.setText("Start time can not be empty");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(this.End.getText().toString().isEmpty()){
            this.Text.setText("End time can not be empty");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(this.Capacity.getText().toString().isEmpty()){
            this.Text.setText("Capacity can not be empty");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        Time start_time = new Time(this.Start.getText().toString());
        Time end_time = new Time(this.End.getText().toString());

        if(!start_time.Isvalid()){
            this.Text.setText("Start time is not valid");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(!end_time.Isvalid()){
            this.Text.setText("end time is not valid");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(start_time.compareTo(end_time) >= 0){
            this.Text.setText("Start time must be earlier than End time");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Time local_time = new Time(format.format(now));

        if(start_time.compareTo(local_time) <= 0){
            this.Text.setText("Time has passed");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(!Location_List.contains(this.Location.getText().toString())){
            this.Text.setText("Location does not exist");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(this.location.equals(this.Location.getText().toString())){
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", this.Name.getText().toString());
            map.put("capacity", Integer.parseInt(this.Capacity.getText().toString()));
            map.put("start", this.Start.getText().toString());
            map.put("end",this.End.getText().toString());

            rf = remote.getAvenueRef(this.location).child(this.id);
            rf.updateChildren(map);
            this.name = map.get("name").toString();
            this.capacity = Integer.parseInt(map.get("capacity").toString());
            this.start = map.get("start").toString();
            this.end = map.get("end").toString();

            NonEditable(this.Name);
            NonEditable(this.Capacity);
            NonEditable(this.Start);
            NonEditable(this.End);
            NonEditable(this.Location);

            this.Modify.setText("Modify");

            this.Delete.setVisibility(View.VISIBLE);

            this.Text.setText("Modify success");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        String id1 = new String(id);

        Delete(id1);

        DatabaseReference rf = remote.getAvenueRef(Location.getText().toString()).push();
        Event event = new Event(rf.getKey(), Name.getText().toString(),
                Integer.parseInt(Capacity.getText().toString()),
                Integer.parseInt(Joined.getText().toString()),
                Start.getText().toString(), End.getText().toString(),
                Location.getText().toString());
        rf.setValue(event);
        Map<String, Object> map = new LinkedHashMap<>();
        for(String user: users){
            map.put(user, "");
        }
        rf.updateChildren(map);

        id = rf.getKey();
        location = Location.getText().toString();
        name = Name.getText().toString();
        capacity = Integer.parseInt(Capacity.getText().toString());
        start = Start.getText().toString();
        end = End.getText().toString();

        Id.setText(id);

        NonEditable(Name);
        NonEditable(Capacity);
        NonEditable(Start);
        NonEditable(End);
        NonEditable(Location);

        Modify.setText("Modify");

        Delete.setVisibility(View.VISIBLE);

        Text.setText("Modify success");
        Text.setVisibility(View.VISIBLE);

    }
}