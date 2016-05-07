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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

public class Login extends Activity implements View.OnClickListener{


    Button bLogin;
    EditText etEmail, etPassword;
    TextView registerLink;
    UserLocalStore userLocalStore;


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogin:
                LoginTask loginTask = new LoginTask();
                loginTask.execute(URL.LOGIN_URL);
                break;
            case R.id.registerLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        registerLink = (TextView) findViewById(R.id.registerLink);

        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

        if(userLocalStore.getUserLoggedIn()){
            User loggedIn = userLocalStore.getLoggedInUser();
            Integer userType = loggedIn.userType;
            if(userType == 1){
                startActivity(new Intent(this, StudentMainActivity.class));

            }
            else if (userType == 2){
                startActivity(new Intent(this, TAMainActivity.class));
            }
            else if (userType == 3){
                startActivity(new Intent(this, ProfessorMainActivity.class));
            }
            else{
                Log.d("bad", "something went wrong retrieving userType");
            }
        }

    }

    /** ASYNC TASK FOLLOWS **/

    class LoginTask extends AsyncTask<String, Void, String> {
        ProgressDialog loading;
        DBConnectionClass luc = new DBConnectionClass();
        Boolean result = false;
        String dbResponse = "";
        Gson gson = new Gson();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        @Override
        protected void onPreExecute() {
            Log.d("preExecute","here");
            super.onPreExecute();
            loading = ProgressDialog.show(Login.this, "Logging In",null, true, true);
            //loading.setMessage("Logging In");
            //loading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String,String>();
            data.put("email", email);
            data.put("password", password);
            dbResponse = luc.sendPostRequest(URL.LOGIN_URL,data);
            return dbResponse;
            //dbResponse now has all the info in JSON form
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            LoginFragment fragment = gson.fromJson(dbResponse, LoginFragment.class);
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            if(fragment.getSuccess()){ //correct validation
                userLocalStore.setUserLoggedIn(true);
                Integer userType = Integer.parseInt(fragment.userType);
                UserLocalStore.setName(fragment.name);
                if(userType == 1){
                    User user = new User(fragment.name, fragment.email, password, 1);
                    userLocalStore.storeUserData(user);
                    Intent in = new Intent(Login.this, StudentMainActivity.class);
                    startActivity(in);

                }
                else if (userType == 2){
                    User user = new User(fragment.name, fragment.email, password, 2);
                    userLocalStore.storeUserData(user);
                    Intent in = new Intent(Login.this, TAMainActivity.class);
                    startActivity(in);
                }
                else if (userType == 3){
                    User user = new User(fragment.name, fragment.email, password, 3);
                    userLocalStore.storeUserData(user);
                    Intent in = new Intent(Login.this, ProfessorMainActivity.class);
                    startActivity(in);
                }
                else{
                    Log.d("bad error", "something went wrong");
                }

            }
            else{ //failed
                Toast.makeText(getApplicationContext(), "Bad Credentials, Try Again", Toast.LENGTH_SHORT).show();
            }
        }


    }


}
