package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Remote remote;
    EditText userNameText, passwordText;
    TextView loginText, registerText;
    CheckBox loginAdmin, registerAdmin;
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
        loginAdmin = (CheckBox) findViewById(R.id.loginAdmin);
        registerAdmin = (CheckBox) findViewById(R.id.registerAdmin);
        login = (Button) findViewById(R.id.loginButton);
        signup = (Button) findViewById(R.id.signupButton);
    }

    /** Called when the user taps the Log in button */
    public void login(View view) {
        String username = userNameText.getText().toString();
        String password = passwordText.getText().toString();

        /** Reading. */
        DatabaseReference ref = remote.getAccountRef();
        Query checkUser = ref.orderByChild("username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    userNameText.setError(null);

                    String passwordDB = snapshot.child(username).child("password").getValue(String.class);

                    if (passwordDB.equals(password)) {
                        passwordText.setError(null);

                        /** User or Admin. */
                        final boolean admin = snapshot.child(username).child("admin").getValue(boolean.class);
                        /** Start the User / Admin Activity. */
                        Intent intent;
                        if (admin) {
                            intent = new Intent(getApplicationContext(), AdminActivity.class);
                        }
                        else {
                            intent = new Intent(getApplicationContext(), UserActivity.class);
                        }
                        clearText();
                        startActivity(intent);
                    }
                    else {
                        passwordText.setError("Wrong Password");
                        passwordText.requestFocus();
                    }
                }
                else {
                    userNameText.setError("No such User exist");
                    userNameText.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /** Called when the user taps the Sign Up button */
    public void signUp(View view) {

        String username = userNameText.getText().toString();
        String password = passwordText.getText().toString();
        Boolean adminLogin = loginAdmin.isChecked();
        Boolean adminRegis = registerAdmin.isChecked();

        //Writing to a realtime database
        DatabaseReference ref = remote.getAccountRef();
        User user = new User(username, password,adminRegis);
        ref.child(username).setValue(user);

        clearText();
    }

    /** Display Sign Up button. */
    public void trySignup(View view) {
        clearText();
        registerText.setVisibility(View.INVISIBLE);
        loginText.setVisibility(View.VISIBLE);
        loginAdmin.setVisibility(View.INVISIBLE);
        registerAdmin.setVisibility(View.VISIBLE);
        login.setVisibility(View.INVISIBLE);
        signup.setVisibility(View.VISIBLE);
    }

    /** Display Login button. */
    public void tryLogin(View view) {
        clearText();
        registerText.setVisibility(View.VISIBLE);
        loginText.setVisibility(View.INVISIBLE);
        loginAdmin.setVisibility(View.VISIBLE);
        registerAdmin.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
        signup.setVisibility(View.INVISIBLE);
    }

    /** Clear text entered. */
    private void clearText() {
        userNameText.setText("");
        passwordText.setText("");
    }
}