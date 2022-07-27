package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

    }

    public void addVenue(View view) {
        Intent intent = new Intent(getApplicationContext(), AddNewVenue.class);
        startActivity(intent);
    }
}