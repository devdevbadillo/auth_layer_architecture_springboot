package com.david.auth_layer_architecture.business.interfaces.application;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import org.springframework.security.authentication.BadCredentialsException;

import com.david.auth_layer_architecture.presentation.dto.request.SignInRequest;

public interface IAuthService {
    PairTokenResponse signIn(SignInRequest signInRequest) throws UserNotFoundException, BadCredentialsException, HaveAccessWithOAuth2Exception, UserNotVerifiedException;

    PairTokenResponse refreshToken(String refreshToken);

}
