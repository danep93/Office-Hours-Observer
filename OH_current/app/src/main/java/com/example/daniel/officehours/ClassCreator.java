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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class ClassCreator extends AppCompatActivity implements View.OnClickListener{
    EditText etClassCode;
    EditText etClassPassword;
    EditText etVerifyPassword;
    EditText etVerificationCode;
    Button bGenerateCode;
    Button bCreateClass;
    UserLocalStore userLocalStore;
    String classPassword;
    String verifyPassword;
    String classCode;

    static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    @Override
    public void onClick(View v) {
        classPassword = etClassPassword.getText().toString().trim();
        verifyPassword = etVerifyPassword.getText().toString().trim();
        classCode = etClassCode.getText().toString().trim();
        switch (v.getId()) {
            case R.id.bCreateClass:
                if (!classPassword.equals(verifyPassword)) {
                    Log.d("reach", "inside create class");
                    Toast.makeText(getApplicationContext(), "passwords don't match", Toast.LENGTH_SHORT).show();
                }
                else if (etVerificationCode.getText().toString().length() <= 0){
                    Toast.makeText(getApplicationContext(), "generate TA Code", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("reach","password is " +classPassword + "verify is " + verifyPassword);
                    ClassCreatorTask task = new ClassCreatorTask();
                    task.execute();
                }
                break;
            case R.id.bGenerateCode:
                String verificationCode = this.generateVerificationCode(classCode);
                etVerificationCode.setText(verificationCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_creator);
        etClassCode = (EditText) findViewById(R.id.etCode);
        etClassPassword = (EditText) findViewById(R.id.etClassPassword);
        etVerifyPassword = (EditText) findViewById(R.id.etVerifyPassword);
        etVerificationCode = (EditText) findViewById(R.id.etTAVerificationCode);
        bGenerateCode = (Button) findViewById(R.id.bGenerateCode);

        bCreateClass = (Button) findViewById(R.id.bCreateClass);
        bGenerateCode.setOnClickListener(this);
        bCreateClass.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    public String generateVerificationCode(String classCode){
        StringBuilder sb = new StringBuilder(7);
        //extract numbers from class code
        String codeNumbers = classCode.substring(4); //takes out CMSC or ENME
        sb.append(codeNumbers);
        for( int i = 0; i < 4; i++ ){
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }
        return sb.toString();


    }

    /**ASYNC TASK**/

    class ClassCreatorTask extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        DBConnectionClass luc = new DBConnectionClass();
        String dbResponse = "";
        Gson gson = new Gson();
        String verificationCode = etVerificationCode.getText().toString().trim();
        User loggedInUser = null;

        @Override
        protected void onPreExecute() {
            Log.d("preExecute", "here");
            super.onPreExecute();
            loading = ProgressDialog.show(ClassCreator.this, "Creating Class", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<String,String>();
            data.put("code", classCode);
            data.put("password", classPassword);
            data.put("email", userLocalStore.getLoggedInUser().email);
            data.put("ta_verification", verificationCode);
            dbResponse = luc.sendPostRequest(URL.CREATE_CLASS_URL,data);
            return dbResponse;
        }
        @Override
        protected void onPostExecute(String s) {
            if(dbResponse != null){
                Successful response = gson.fromJson(dbResponse, Successful.class);
                if(response.getSuccess()){
                    loggedInUser = userLocalStore.getLoggedInUser();
                    loggedInUser.addClass(classCode);
                    userLocalStore.storeUserData(loggedInUser);
                    Toast.makeText(getApplicationContext(), "Class Successfully Created", Toast.LENGTH_SHORT).show();
                    Log.d("reach", "in post execute");
                    Log.d("user name",userLocalStore.getLoggedInName());
                    ArrayList<String> classList = userLocalStore.getLoggedInUser().getClassList();
                    for(String course : classList){
                        Log.d("class",course);
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Error: Class could not be created", Toast.LENGTH_SHORT).show();
                }
                Intent in = new Intent(ClassCreator.this, ProfessorMainActivity.class);
                startActivity(in);
            }
            else{
                Log.d("bad","response was null, check connection");
            }
        }
    }
}
