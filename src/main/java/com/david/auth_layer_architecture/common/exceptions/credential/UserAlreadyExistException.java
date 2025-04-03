package com.david.auth_layer_architecture.common.exceptions.credential;

public class UserAlreadyExistException extends Exception {
    
    public UserAlreadyExistException(String message){
        super(message);
    }
}
