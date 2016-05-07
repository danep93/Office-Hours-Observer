package com.example.daniel.officehours;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class TAMainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextName;
    TextView textViewName;
    EditText editTextPassword;
    EditText editTextEmail;
    Button bLogout;
    UserLocalStore userLocalStore;
    Toolbar toolbar;




    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                break;
        }
    }


    @Override
    protected void onStart() {
        //when this opens we need to authenticate user to make sure he's logged in
        super.onStart();
        if(authenticate()){
            displayUserDetails();
        }
        else{
            startActivity(new Intent(this, Login.class));
        }
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();
        editTextName.setText(user.name);
        //editTextName.setText(user.name);
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ta_activity_main);

        editTextName = (EditText) findViewById(R.id.etName);
        //textViewName = (TextView)findViewById(R.id.tvName);
        //textViewName.setText(UserLocalStore.SP_NAME);
        bLogout = (Button) findViewById(R.id.bLogout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        bLogout.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }
}
