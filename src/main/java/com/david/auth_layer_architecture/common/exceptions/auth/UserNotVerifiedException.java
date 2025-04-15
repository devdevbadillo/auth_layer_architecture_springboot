package com.david.auth_layer_architecture.common.exceptions.auth;

public class UserNotVerifiedException extends Exception{

    public UserNotVerifiedException(String message){
        super(message);
    }
}
