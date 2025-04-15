package com.david.auth_layer_architecture.business.service.interfaces;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import org.springframework.security.authentication.BadCredentialsException;

import com.david.auth_layer_architecture.domain.dto.request.SignInRequest;

import java.io.IOException;

public interface IAuthService {
    SignInResponse signIn(SignInRequest signInRequest) throws BadCredentialsException, HaveAccessWithOAuth2Exception, UserNotVerifiedException;

    SignInResponse refreshToken(String refreshToken) throws JWTVerificationException;

}
