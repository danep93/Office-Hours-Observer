package com.example.daniel.officehours;

/**
 * Created by Daniel on 4/22/16.
 */
public class Successful {

    private Boolean success;
    private String message;

    public Successful(Boolean success, String message){

        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess(){
        return success;
    }

    public String getMessage(){
        return this.message;
    }
}
