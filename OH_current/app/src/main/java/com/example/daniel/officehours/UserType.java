package com.example.daniel.officehours;

/**
 * Created by Daniel on 3/17/16.
 */

public enum UserType {
    STUDENT(1),
    TA(2),
    PROFESSOR(3);

    private final int value;

    UserType(final int value){
        this.value = value;
    }
    public static UserType getUserType(int value){
        for(UserType u : UserType.values()){
            if(u.value == value){
                return u;
            }
        }
        throw new IllegalArgumentException("user type not found");
    }
    public static int getIntFromUserType(UserType userType){
        return userType.value;
    }
}