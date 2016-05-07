package com.example.daniel.officehours;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Daniel on 3/17/16.
 */
public class UserLocalStore {
    public static String SP_NAME;
    public static int idCounter = 1;
    SharedPreferences userLocalDatabase;

    public static void setName(String name){
        SP_NAME = name;
    }


    public UserLocalStore(Context context) {
        //all activities that use this java class need to give us their context
        // so we can use them to make the sharedPreference
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit(); //can edit attributes
        spEditor.putString("name", user.name);
        spEditor.putString("email", user.email);
        spEditor.putString("password", user.password);
        spEditor.putInt("userType", user.userType);
        for(String s : user.getClassList()){
            storeUserClass(s);
        }
        spEditor.commit();
    }

    public String getLoggedInName(){
        if(userLocalDatabase.getBoolean("loggedIn", false) == true){
            return userLocalDatabase.getString("name", "");
        }
        else{
            return null;
        }
    }

    public void storeUserClass(String classCode){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit(); //can edit attributes
        spEditor.putString("class_"+idCounter, classCode);
        spEditor.commit();
        idCounter++;
    }

    public User getLoggedInUser() {
        String name = userLocalDatabase.getString("name", ""); //default value is nothing
        String password = userLocalDatabase.getString("password", "");
        String email = userLocalDatabase.getString("email", "");
        Integer userType =userLocalDatabase.getInt("userType", 0);
        User storedUser = new User(name, email, password, userType);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        //if user is logged in parameter value is TRUE
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        idCounter = 1;
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("loggedIn", false) == true) //making default false
            return true;
        else
            return false;
    }
}
