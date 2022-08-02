package com.example.sport_events_scheduler;

import java.util.ArrayList;

public class Event {

    private String id;
    private String name;
    private int capacity;
    private int joined;
    private String start;
    private String end;
    private String location;

    public Event() {}

    public Event(String id, String name, int capacity, int joined, String start, String end, String location) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.joined = joined;
        this.start = start;
        this.end = end;
        this.location = location;
    }

    public String getId() { return id; }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getJoined() {
        return joined;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getLocation() {
        return location;
    }

}
