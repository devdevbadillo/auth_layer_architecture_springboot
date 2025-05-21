package com.david.auth_layer_architecture.business.facade.implementation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.david.auth_layer_architecture.business.facade.interfaces.IAuthFacade;
import com.david.auth_layer_architecture.business.service.interfaces.IAuthService;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.request.SignInRequest;

@Service
@AllArgsConstructor
public class AuthFacadeImpl implements IAuthFacade{

    private final IAuthService authService;

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) throws UserNotFoundException, BadCredentialsException, HaveAccessWithOAuth2Exception, UserNotVerifiedException {
        return this.authService.signIn(signInRequest);
    }

    @Override
    public SignInResponse refreshToken(String refreshToken) {
        return this.authService.refreshToken(refreshToken);
    }
}
