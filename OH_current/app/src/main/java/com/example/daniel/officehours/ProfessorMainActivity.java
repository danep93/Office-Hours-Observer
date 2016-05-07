package com.example.daniel.officehours;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProfessorMainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextName;
    TextView textViewName;
    EditText editTextPassword;
    EditText editTextEmail;
    Button bLogout;
    UserLocalStore userLocalStore;
    Toolbar toolbar;
    ImageButton bPlus;
    Button bAddTA;




    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.bPlus:
                startActivity(new Intent(this, ClassCreator.class));
                break;
            case R.id.bAddTa:
                if(userLocalStore.getLoggedInUser().getClassList().size() > 0){
                    Intent in = new Intent(this, TAAdder.class);
                    in.putExtra("classList", userLocalStore.getLoggedInUser().getClassList());
                    startActivity(in);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Must create class before adding TA", Toast.LENGTH_SHORT).show();
                }
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
        setContentView(R.layout.professor_activity_main);

        editTextName = (EditText) findViewById(R.id.etName);
        //textViewName = (TextView)findViewById(R.id.tvName);
        //textViewName.setText(UserLocalStore.SP_NAME);
        bLogout = (Button) findViewById(R.id.bLogout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bPlus = (ImageButton) findViewById(R.id.bPlus);
        bAddTA = (Button) findViewById(R.id.bAddTa);
        userLocalStore = new UserLocalStore(this);

        bPlus.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bAddTA.setOnClickListener(this);


    }
}
