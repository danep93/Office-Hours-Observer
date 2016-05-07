package com.example.daniel.officehours;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class StudentMainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextName;
    //EditText editTextEmail;
    Button bLogout;
    UserLocalStore userLocalStore;
    Toolbar toolbar;
    ImageButton bPlus;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogout:
                Log.d("reached here", "in onClick logout");
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.bPlus:
                Log.d("reached here", "in onClick plusButton");
                Intent in = new Intent(this, StudentClassAdder.class);
                in.putExtra("studentEmail", userLocalStore.getLoggedInUser().email);
                startActivity(in);
                break;
        }
    }


    @Override
    protected void onStart() {
        //when this opens we need to authenticate user to make sure he's logged in
        super.onStart();
        if (authenticate()) {
            displayUserDetails();
        } else {
            startActivity(new Intent(this, Login.class));
        }
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        editTextName.setText(user.name);
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this);
        setContentView(R.layout.student_activity_main);

        editTextName = (EditText) findViewById(R.id.etName);
        bLogout = (Button) findViewById(R.id.bLogout);
        bPlus = (ImageButton) findViewById(R.id.bPlus);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        bLogout.setOnClickListener(this);
        bPlus.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }

}
