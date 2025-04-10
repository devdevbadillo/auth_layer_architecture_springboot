package com.david.auth_layer_architecture.common.exceptions.accessToken;

public class AlreadyHaveAccessTokenToChangePasswordException extends Exception {

    public AlreadyHaveAccessTokenToChangePasswordException(String message) {
        super(message);
    }
}
