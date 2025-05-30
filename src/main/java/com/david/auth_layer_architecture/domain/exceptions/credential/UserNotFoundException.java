package com.david.auth_layer_architecture.domain.exceptions.credential;

public class UserNotFoundException extends Exception{
    
    public UserNotFoundException(String message){
        super(message);
    }
}
