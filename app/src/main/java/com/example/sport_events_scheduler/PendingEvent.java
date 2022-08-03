package com.example.sport_events_scheduler;

import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class PendingEvent {

//    @Exclude
//    private String key;
    private int capacity, joined;
    private String end, id, location, name, start;

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getJoined() {
        return joined;
    }

    public String getId() {
        return id;
    }
}
