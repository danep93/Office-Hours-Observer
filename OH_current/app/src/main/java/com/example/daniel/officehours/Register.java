package com.example.daniel.officehours;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Register extends Activity implements View.OnClickListener{


    View selectedRadio;
    Button bRegister;
    EditText etName, etEmail, etPassword, etConfirmPassword;
    private Integer userType = 0; //by default its not Student nor TA nor Professor
    //private static final String REGISTER_URL = "http://www.officehoursobserver.com/register.php";

    public void onRadioButtonClicked(View view){
       selectedRadio = view;
    }



    @Override
    public void onClick(View v) {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        User user = null;
        if(name.equals(""))
            Toast.makeText(getApplicationContext(), "name is empty", Toast.LENGTH_SHORT).show();
        else if (email.equals(""))
            Toast.makeText(getApplicationContext(), "email is empty", Toast.LENGTH_SHORT).show();
        else if (!password.equals(confirmPassword))
            Toast.makeText(getApplicationContext(), "passwords don't match", Toast.LENGTH_SHORT).show();
        else{ //everything is fine. Maybe more else ifs for PW length and email verification

            switch(selectedRadio.getId()){
                case R.id.radioStudent:
                    user = new User(name, email, password, 1);
                    userType = 1;
                    Log.d("student","chose student");
                    break;
                case R.id.radioTA:
                    user = new User(name, email, password, 2);
                    userType = 2;
                    Log.d("TA","chose TA");
                    break;
                case R.id.radioProfessor:
                    user = new User(name, email, password, 3);
                    userType = 3;
                    Log.d("professor", "chose professor");
                    //startActivity(new Intent(this, ProfessorMainActivity.class));
                    break;
            }

            registerUser();
            startActivity(new Intent(this, Login.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        selectedRadio = findViewById(R.id.radioStudent); //default
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);
    }

    private void registerUser() {
        Log.d("another debug", "in registerUser");
        String name = etName.getText().toString().trim().toLowerCase();
        String password = etPassword.getText().toString().trim().toLowerCase();
        String email = etEmail.getText().toString().trim().toLowerCase();
        if (userType == 0){
            Log.d("bad userType", "UT is still 0, something's wrong");
        }

        register(name, password, email); //add userType to this
    }


    private void register(String name, String password, String email) {
        class RegisterUser extends AsyncTask<String, Void, String> { //maybe add URL?
            ProgressDialog loading;
            DBConnectionClass ruc = new DBConnectionClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("email",params[1]);
                data.put("password",params[2]);
                data.put("user_type", String.valueOf(userType));
                Log.d("usertypeNumber", "userType is " + String.valueOf(userType));


                String result = ruc.sendPostRequest(URL.REGISTER_URL,data);
                Log.d("backgroundDebug", "result is " + result);
                if(result == null){
                    result = "Error registering";
                }
                return  result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Success Registering",Toast.LENGTH_LONG).show();
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name, email, password, String.valueOf(userType));
    }
}
