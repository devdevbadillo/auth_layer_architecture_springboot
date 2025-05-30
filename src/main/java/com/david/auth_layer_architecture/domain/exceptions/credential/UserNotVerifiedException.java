package com.david.auth_layer_architecture.domain.exceptions.credential;

public class UserNotVerifiedException extends Exception{

    public UserNotVerifiedException(String message){
        super(message);
    }
}
