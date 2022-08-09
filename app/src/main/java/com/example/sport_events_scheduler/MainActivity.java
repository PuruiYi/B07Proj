package com.example.sport_events_scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Remote remote;
    private EditText userNameText, passwordText;
    private TextView loginText, signupText, loginHint, signupHint;
    private ImageView loginPic, signupPic;
    private Button login, signup;
    private CheckBox asAdmin;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Initializer. */
        remote = new Remote();
        userNameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        loginText = findViewById(R.id.loginText);
        signupText = findViewById(R.id.registerText);
        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signupButton);
        asAdmin = findViewById(R.id.asAdmin);
        loginPic = findViewById(R.id.loginPic);
        loginHint = findViewById(R.id.loginHint);
        signupPic = findViewById(R.id.signupPic);
        signupHint = findViewById(R.id.signupHint);

        asAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                asAdmin.setError(null);
            }
        });

        /** Retrieve and cache the system's default "long" animation time. */
        duration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

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
                        final boolean isAdmin = snapshot.child(username).child("admin").getValue(boolean.class);
                        /** Start the User / Admin Activity. */
                        Intent intent;
                        if (isAdmin && asAdmin.isChecked()) {
                            intent = new Intent(getApplicationContext(), AdminActivity.class);
                        }
                        else if (!isAdmin && asAdmin.isChecked()) {
                            asAdmin.setError("This user is not an admin", null);
                            asAdmin.requestFocus();
                            return;
                        }
                        else {
                            intent = new Intent(getApplicationContext(), UserActivity.class);
                        }
                        /** Pass username. */
                        intent.putExtra("user", username);
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
        /** Retrieve inputs from the View. */
        String username = userNameText.getText().toString();
        String password = passwordText.getText().toString();
        /** Database. */
        DatabaseReference ref = remote.getAccountRef();
        /** Validate if the current username has been registered. */
        Query checkUsername = ref.orderByKey().equalTo(username);
        checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /** The username has been registered. */
                if (snapshot.exists()) {
                    userNameText.setError("The username has been registered");
                    userNameText.requestFocus();
                }
                /** Not yet. */
                else{
                    /** Validate inputs. */
                    if (userNameText.getError() == null && areValidInputs(username, password)) {
                        /** Clear errors. */
                        userNameText.setError(null);
                        passwordText.setError(null);
                        /** Sign up as admin? */
                        Account user;
                        if (asAdmin.isChecked()) {
                            user = new Admin(username, password);
                        }
                        else {
                            user = new User(username, password);
                        }
                        ref.child(username).setValue(user);
                        Toast.makeText(getApplicationContext(), "Signup Successfully", Toast.LENGTH_SHORT).show();
                    }
                    clearText();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /** Validate username and password provided by users. */
    private boolean areValidInputs(String username, String password) {
        Pattern pattern = Pattern.compile("\\s*");
        Matcher usernameM = pattern.matcher(username);
        Matcher passwordM = pattern.matcher(password);
        if (usernameM.matches()) {
            userNameText.setError("Username cannot be empty.");
            userNameText.requestFocus();
            return false;
        }
        if (passwordM.matches()) {
            passwordText.setError("Password cannot be empty.");
            passwordText.requestFocus();
            return false;
        }
        return true;
    }

    /** Display Sign Up button. */
    public void trySignup(View view) {
        clearText();

        CrossFade.animate(signupHint, loginHint, duration);
        CrossFade.animate(signupPic, loginPic, duration);
        CrossFade.animate(loginText, signupText, duration);
        CrossFade.animate(signup, login, duration);

    }

    /** Display Login button. */
    public void tryLogin(View view) {
        clearText();

        CrossFade.animate(loginHint, signupHint, duration);
        CrossFade.animate(loginPic, signupPic, duration);
        CrossFade.animate(signupText, loginText, duration);
        CrossFade.animate(login, signup, duration);

    }

    /** Clear text entered. */
    private void clearText() {
        userNameText.setText("");
        userNameText.setError(null);
        passwordText.setText("");
        passwordText.setError(null);
        asAdmin.setChecked(false);
    }



}