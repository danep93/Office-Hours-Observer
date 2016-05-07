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

public class StudentClassAdder extends AppCompatActivity implements View.OnClickListener {

    EditText etClassCode;
    EditText etClassPassword;
    String studentEmail = null;
    UserLocalStore userLocalStore;
    Button bAddClass;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAddClass:
                PasswordValidationTask passwordTask = new PasswordValidationTask();
                passwordTask.execute(URL.CREATE_CLASS_URL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_adder);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            studentEmail = extra.getString("studentEmail");
        }
        userLocalStore = new UserLocalStore(this);
        etClassCode = (EditText) findViewById(R.id.etCode);
        etClassPassword = (EditText) findViewById(R.id.etClassPassword);
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

        @Override
        protected void onPreExecute() {
            Log.d("preExecute", "here");
            super.onPreExecute();
            loading = ProgressDialog.show(StudentClassAdder.this, "Validating Password",null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<String,String>();
            data.put("code", classCode);
            data.put("password", classPassword);
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
                Intent in = new Intent(StudentClassAdder.this, StudentClassAdder.class);
                in.putExtra("studentEmail", studentEmail);
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(StudentClassAdder.this, "Adding Class",null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<String,String>();
            data.put("code", classCode);
            data.put("password", classPassword);
            data.put("email", studentEmail);
            dbResponse = luc.sendPostRequest(URL.ADD_CLASS_URL,data);
            return dbResponse;

        }
        @Override
        protected void onPostExecute(String s) {
            Successful response = gson.fromJson(dbResponse, Successful.class);
            if(response.getSuccess()){
                User loggedInUser = userLocalStore.getLoggedInUser();
                loggedInUser.addClass(classCode);
                userLocalStore.storeUserClass(classCode);
                Toast.makeText(getApplicationContext(), "Class Succesfully Added", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Error: Class could not be added", Toast.LENGTH_SHORT).show();
            }
            Intent in = new Intent(StudentClassAdder.this, StudentMainActivity.class);
            startActivity(in);

        }
    }
}
