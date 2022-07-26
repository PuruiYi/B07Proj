package com.example.sport_events_scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Remote remote;
    EditText userNameText, passwordText;
    TextView loginText, registerText;
    Button login, signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Firebase realtime database reference. */
        remote = new Remote();
        userNameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        loginText = (TextView) findViewById(R.id.loginText);
        registerText = (TextView) findViewById(R.id.registerText);
        login = (Button) findViewById(R.id.loginButton);
        signup = (Button) findViewById(R.id.signupButton);
    }

    /** Called when the user taps the Sign Up button */
    public void signUp(View view) {

        String username = userNameText.getText().toString();
        String password = passwordText.getText().toString();

        //Writing to a realtime database
        DatabaseReference ref = remote.getAccountRef();
        User user = new User(username, password);
        ref.push().setValue(user);
    }

    /** Display Sign Up button. */
    public void trySignup(View view) {
        registerText.setVisibility(View.INVISIBLE);
        loginText.setVisibility(View.VISIBLE);
        login.setVisibility(View.INVISIBLE);
        signup.setVisibility(View.VISIBLE);
    }

    /** Display Login button. */
    public void tryLogin(View view) {
        registerText.setVisibility(View.VISIBLE);
        loginText.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
        signup.setVisibility(View.INVISIBLE);
    }
}