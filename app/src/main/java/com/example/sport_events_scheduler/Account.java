package com.example.sport_events_scheduler;

public abstract class Account {

    private String username;
    private String password;
    private boolean admin;

    public Account() {}

    public Account(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
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

}
