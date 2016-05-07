package com.example.daniel.officehours;

/**
 * Created by Daniel on 4/19/16.
 */
public class LoginFragment {

    String name;
    String email;
    String userType;
    boolean success;

    public LoginFragment(String name, String email, String userType, boolean success){
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.success = success;
    }

    public boolean getSuccess(){
        return this.success;
    }
}
