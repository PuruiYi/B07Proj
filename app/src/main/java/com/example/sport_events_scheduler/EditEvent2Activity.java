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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class EditEvent2Activity extends AppCompatActivity {
    String name;
    int capacity;
    int joined;
    String location;
    String start;
    String end;
    String id;
    String date;
    ArrayList<String> usernames;
    EditText Name;
    EditText Capacity;
    EditText Joined;
    EditText Location;
    EditText Start;
    EditText End;
    EditText Id;
    EditText Date;
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
        this.date = intent.getStringExtra("date");
        this.usernames = intent.getStringArrayListExtra("usernames");

        this.Name = (EditText)findViewById(R.id.eventNameMg);
        this.Capacity = (EditText)findViewById(R.id.eventCapacityMg);
        this.Joined = (EditText)findViewById(R.id.eventJoinedMg);
        this.Location = (EditText)findViewById(R.id.eventLocationMg);
        this.Start = (EditText)findViewById(R.id.eventStartAt);
        this.End = (EditText)findViewById(R.id.eventEndAt);
        this.Id = (EditText)findViewById(R.id.eventIdMg);
        this.Text = (TextView)findViewById(R.id.edittext4);
        this.Date = (EditText)findViewById(R.id.eventDate);

        Linear = (LinearLayout)findViewById(R.id.linearLayout);

        this.Name.setText(this.name);
        this.Capacity.setText(String.valueOf(this.capacity));
        this.Joined.setText(String.valueOf(this.joined));
        this.Location.setText(this.location);
        this.Start.setText(this.start);
        this.End.setText(this.end);
        this.Id.setText(this.id);
        this.Date.setText(this.date);

        NonEditable(this.Name);
        NonEditable(this.Capacity);
        NonEditable(this.Joined);
        NonEditable(this.Location);
        NonEditable(this.Start);
        NonEditable(this.End);
        NonEditable(this.Id);
        NonEditable(this.Date);

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
        if(Delete.getText().toString().equals("Delete")) {
            String id1 = new String(this.id);
            Delete(id1);
            Linear.removeViews(0, 9);
            this.Text.setText("Delete success. Back to prev page in 5 seconds.");
            this.Text.setVisibility(View.VISIBLE);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    EditEvent2Activity.this.finish();
                }
            }, 5000);
        }
        else{
            this.Name.setText(this.name);
            this.Capacity.setText(String.valueOf(this.capacity));
            this.Location.setText(this.location);
            this.Start.setText(this.start);
            this.End.setText(this.end);
            this.Date.setText(this.date);

            Modify.setText("Modify");;
            Delete.setText("Delete");

            NonEditable(this.Name);
            NonEditable(this.Capacity);
            NonEditable(this.Location);
            NonEditable(this.Start);
            NonEditable(this.End);
            NonEditable(this.Date);

        }
    }

    @SuppressLint("SetTextI18n")
    public void Modify_Event(View view){
        if(this.Modify.getText().toString().equals("Modify")){
            Editable(this.Capacity);
            Editable(this.Name);
            Editable(this.Start);
            Editable(this.End);
            Editable(this.Location);
            Editable(this.Date);
            this.Modify.setText("Submit");
            this.Delete.setText("Cancel");
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

        if(this.Date.getText().toString().isEmpty()){
            this.Text.setText("Date can not be empty");
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

        Time start_time = new Time(this.Start.getText().toString(), this.Date.getText().toString());
        Time end_time = new Time(this.End.getText().toString(), this.Date.getText().toString());

        if(!start_time.Time_Valid()){
            this.Text.setText("Start time is not valid");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(!end_time.Time_Valid()){
            this.Text.setText("End time is not valid");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(!start_time.Date_Valid()){
            this.Text.setText("Date is not valid");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        if(start_time.compareTo(end_time) >= 0){
            this.Text.setText("Start time must be earlier than End time");
            this.Text.setVisibility(View.VISIBLE);
            return;
        }

        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat time_format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        Time local_time = new Time(time_format.format(now), date_format.format(now));

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

        rf = remote.getAvenueRef(this.Location.getText().toString());

        if(this.location.equals(this.Location.getText().toString())){

            rf.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot datasnapshot: snapshot.getChildren()){
                        //TODO: just for test
                        if(!datasnapshot.hasChild("date"))
                            continue;

                        if(datasnapshot.getKey().equals(id))
                            continue;

                        Time event2_start = new Time(datasnapshot.child("start").getValue().toString(),
                                datasnapshot.child("date").getValue().toString());
                        Time event2_end = new Time(datasnapshot.child("end").getValue().toString(),
                                datasnapshot.child("date").getValue().toString());

                        if(Time.Time_Conflict(start_time, end_time, event2_start, event2_end)){
                            rf.removeEventListener(this);
                            Text.setText("Time conflict");
                            Text.setVisibility(View.VISIBLE);
                            return;
                        }
                    }

                    rf.removeEventListener(this);

                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("name", Name.getText().toString());
                    map.put("capacity", Integer.parseInt(Capacity.getText().toString()));
                    map.put("date", Date.getText().toString());
                    map.put("start", Start.getText().toString());
                    map.put("end",End.getText().toString());

                    rf = remote.getAvenueRef(location).child(id);
                    rf.updateChildren(map);
                    name = map.get("name").toString();
                    capacity = Integer.parseInt(map.get("capacity").toString());
                    start = map.get("start").toString();
                    end = map.get("end").toString();
                    date = map.get("date").toString();

                    NonEditable(Name);
                    NonEditable(Capacity);
                    NonEditable(Start);
                    NonEditable(End);
                    NonEditable(Location);
                    NonEditable(Date);

                    Modify.setText("Modify");
                    Delete.setText("Delete");

                    Text.setText("Modify success. Back to prev page in 5 seconds.");
                    Text.setVisibility(View.VISIBLE);

                    Modify.setClickable(false);
                    Delete.setClickable(false);

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            EditEvent2Activity.this.finish();
                        }
                    }, 5000);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return;

        }

        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot: snapshot.getChildren()){
                    //TODO: just for test
                    if(!datasnapshot.hasChild("date"))
                        continue;

                    Time event2_start = new Time(datasnapshot.child("start").getValue().toString(),
                            datasnapshot.child("date").getValue().toString());
                    Time event2_end = new Time(datasnapshot.child("end").getValue().toString(),
                            datasnapshot.child("date").getValue().toString());

                    if(Time.Time_Conflict(start_time, end_time, event2_start, event2_end)){
                        rf.removeEventListener(this);
                        Text.setText("Time conflict");
                        Text.setVisibility(View.VISIBLE);
                        return;
                    }
                }

                rf.removeEventListener(this);

                String id1 = new String(id);

                Delete(id1);

                DatabaseReference rf = remote.getAvenueRef(Location.getText().toString()).push();
                Event event = new Event(rf.getKey(), Name.getText().toString(),
                        Integer.parseInt(Capacity.getText().toString()),
                        Integer.parseInt(Joined.getText().toString()),
                        Date.getText().toString(), Start.getText().toString(),
                        End.getText().toString(), Location.getText().toString());
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
                date = Date.getText().toString();

                Id.setText(id);

                NonEditable(Name);
                NonEditable(Capacity);
                NonEditable(Start);
                NonEditable(End);
                NonEditable(Location);
                NonEditable(Date);

                Modify.setText("Modify");
                Delete.setText("Delete");

                Text.setText("Modify success. Back to previous page in 5 seconds.");
                Text.setVisibility(View.VISIBLE);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        EditEvent2Activity.this.finish();
                    }
                }, 5000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}