package com.david.auth_layer_architecture.business.service.interfaces;

import org.springframework.security.authentication.BadCredentialsException;

import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.request.SignInRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

public interface IAuthService {
    MessageResponse signIn(SignInRequest signInRequest) throws BadCredentialsException;
}
