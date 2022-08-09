package com.example.sport_events_scheduler;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Event implements Parcelable {

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

    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        capacity = in.readInt();
        joined = in.readInt();
        start = in.readString();
        end = in.readString();
        location = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeInt(capacity);
        parcel.writeInt(joined);
        parcel.writeString(start);
        parcel.writeString(end);
        parcel.writeString(location);
    }
}
