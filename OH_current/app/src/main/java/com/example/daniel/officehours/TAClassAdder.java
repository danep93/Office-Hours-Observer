package com.example.daniel.officehours;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class TAClassAdder extends AppCompatActivity implements View.OnClickListener {

    EditText etClassCode;
    EditText etClassPassword;
    EditText etVerificationCode;
    String taEmail = null;
    UserLocalStore userLocalStore;
    Button bAddClass;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAddClass:
                PasswordValidationTask passwordTask = new PasswordValidationTask();
                passwordTask.execute(URL.ADD_CLASS_URL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_class_adder);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            taEmail = extra.getString("taEmail");
        }
        userLocalStore = new UserLocalStore(this);
        etClassCode = (EditText) findViewById(R.id.etCode);
        etClassPassword = (EditText) findViewById(R.id.etClassPassword);
        etVerificationCode = (EditText) findViewById(R.id.etVerificationCode);
        bAddClass = (Button) findViewById(R.id.bAddClass);
        bAddClass.setOnClickListener(this);
    }

    /** ASYNC TASK **/

    class PasswordValidationTask extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        DBConnectionClass luc = new DBConnectionClass();
        String dbResponse = "";
        Gson gson = new Gson();
        String classPassword = etClassPassword.getText().toString().trim();
        String classCode = etClassCode.getText().toString().trim();
        String taVerificationCode = etVerificationCode.toString().trim();

        @Override
        protected void onPreExecute() {
            Log.d("preExecute", "here");
            super.onPreExecute();
            loading = ProgressDialog.show(TAClassAdder.this, "Validating Credentials",null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<String,String>();
            data.put("code", classCode);
            data.put("password", classPassword);
            data.put("verification_code", taVerificationCode);
            dbResponse = luc.sendPostRequest(URL.FIND_CLASS_URL,data);
            return dbResponse;

        }
        @Override
        protected void onPostExecute(String s) {
            Successful response = gson.fromJson(dbResponse, Successful.class);
            if (response.getSuccess()){
                AddClassTask task = new AddClassTask();
                task.execute();
            }
            else{
                Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                Intent in = new Intent(TAClassAdder.this, TAClassAdder.class);
                in.putExtra("taEmail", taEmail);
                startActivity(in);
            }

        }
    }

    /** SECOND ASYNC TASK **/

    class AddClassTask extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        DBConnectionClass luc = new DBConnectionClass();
        Boolean result = false;
        String dbResponse = "";
        Gson gson = new Gson();
        String classPassword = etClassPassword.getText().toString().trim();
        String classCode = etClassCode.getText().toString().trim();
        String taVerificationCode = etVerificationCode.toString().trim();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(TAClassAdder.this, "Adding Class",null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<String,String>();
            data.put("code", classCode);
            data.put("password", classPassword);
            data.put("email", taEmail);
            data.put("verification", taVerificationCode);
            dbResponse = luc.sendPostRequest(URL.TA_ADD_CLASS_URL,data);
            return dbResponse;

        }
        @Override
        protected void onPostExecute(String s) {
            Successful response = gson.fromJson(dbResponse, Successful.class);
            if(response.getSuccess()){
                User loggedInUser = userLocalStore.getLoggedInUser();
                loggedInUser.addClass(classCode);
                userLocalStore.storeUserData(loggedInUser);
                Toast.makeText(getApplicationContext(), "Class Succesfully Added", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Error: Class could not be added", Toast.LENGTH_SHORT).show();
            }
            Intent in = new Intent(TAClassAdder.this, TAMainActivity.class);
            startActivity(in);

        }
    }
}
