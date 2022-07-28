package com.example.sport_events_scheduler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Remote {

    private FirebaseDatabase remote;

    public Remote() {
        remote = FirebaseDatabase.getInstance("https://sport-events-scheduler-default-rtdb.firebaseio.com/");
    }

    public DatabaseReference getAccountRef() {
        return remote.getReference("account");
    }

    public DatabaseReference getEventRef() {
        return remote.getReference("event");
    }

    public DatabaseReference getVenueRef(String venue) {
        return remote.getReference("event/" + venue);
    }

    public DatabaseReference getUserRef(String user) {
        return remote.getReference("account/" + user);
    }
}
