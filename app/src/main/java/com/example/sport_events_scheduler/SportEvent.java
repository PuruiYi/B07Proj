package com.example.sport_events_scheduler;

public abstract class SportEvent {

    private String name;
    private int capacity;
    private int joined;
    private String start;
    private String end;
    private String location;

    public SportEvent() {}

    public SportEvent(String name, int capacity, int joined, String start, String end, String location) {
        this.name = name;
        this.capacity = capacity;
        this.joined = joined;
        this.start = start;
        this.end = end;
        this.location = location;
    }

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
