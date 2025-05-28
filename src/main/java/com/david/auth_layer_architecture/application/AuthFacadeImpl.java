package com.david.auth_layer_architecture.application;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.david.auth_layer_architecture.domain.application.IAuthFacade;
import com.david.auth_layer_architecture.business.interfaces.application.IAuthService;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.presentation.dto.request.SignInRequest;

@Service
@AllArgsConstructor
public class AuthFacadeImpl implements IAuthFacade{
    private final IAuthService authService;

    @Override
    public PairTokenResponse signIn(SignInRequest signInRequest) throws UserNotFoundException, BadCredentialsException, HaveAccessWithOAuth2Exception, UserNotVerifiedException {
        return this.authService.signIn(signInRequest);
    }

    @Override
    public PairTokenResponse refreshToken(String refreshToken) {
        return this.authService.refreshToken(refreshToken);
    }
}
