package com.example.sport_events_scheduler;

import java.util.ArrayList;

public abstract class Account {

    private String username;
    private String password;
    private boolean admin;
    private ArrayList<String> eventsJoined;

    public Account() {}

    public Account(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.eventsJoined = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public ArrayList<String> getEventsJoined() { return eventsJoined; }

}
