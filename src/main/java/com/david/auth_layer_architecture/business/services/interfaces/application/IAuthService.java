package com.david.auth_layer_architecture.business.services.interfaces.application;

import com.david.auth_layer_architecture.domain.exceptions.auth.HasAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotVerifiedException;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import org.springframework.security.authentication.BadCredentialsException;

import com.david.auth_layer_architecture.presentation.dto.request.SignInRequest;

public interface IAuthService {
    PairTokenResponse signIn(SignInRequest signInRequest) throws UserNotFoundException, BadCredentialsException, HasAccessWithOAuth2Exception, UserNotVerifiedException;

    PairTokenResponse refreshToken(String refreshToken);

}
