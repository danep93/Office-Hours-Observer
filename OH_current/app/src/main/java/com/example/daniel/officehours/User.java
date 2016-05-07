package com.example.daniel.officehours;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 3/17/16.
 */
public class User {

    String name;
    String email;
    String password;
    Integer userType;
    ArrayList<String> classList = new ArrayList<String>();


    public User(String name, String email, String password, Integer userType){
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public void addClass(String classCode){
        classList.add(classCode);
    }

    public ArrayList<String> getClassList(){
        return this.classList;
    }
}
