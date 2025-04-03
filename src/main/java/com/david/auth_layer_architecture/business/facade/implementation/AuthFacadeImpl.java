package com.david.auth_layer_architecture.business.facade.implementation;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.david.auth_layer_architecture.business.facade.interfaces.IAuthFacade;
import com.david.auth_layer_architecture.business.service.interfaces.IAuthService;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.request.SignInRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

@Service
@AllArgsConstructor
public class AuthFacadeImpl implements IAuthFacade{

    private final IAuthService authService;

    @Override
    public MessageResponse signIn(SignInRequest signInRequest) throws UserNotFoundException, BadCredentialsException {
        return this.authService.signIn(signInRequest);
    }
    
}
