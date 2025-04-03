package com.david.auth_layer_architecture.business.facade.implementation;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.david.auth_layer_architecture.business.facade.interfaces.ICredentialFacade;
import com.david.auth_layer_architecture.business.service.interfaces.ICredentialService;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.domain.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;

@Service
@AllArgsConstructor
public class CredentialFacadeImpl implements ICredentialFacade{

    private final ICredentialService credentialService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MessageResponse signUp(SignUpRequest signUpRequest) throws UserAlreadyExistException {
        Credential credential = this.buildCredential(signUpRequest);
        return credentialService.signUp(credential);
    }

    private Credential buildCredential(SignUpRequest signUpRequest){
        String passwordHash = passwordEncoder.encode(signUpRequest.getPassword());
        return Credential.builder()
                .email(signUpRequest.getEmail())
                .password(passwordHash)
                .build();
    }
}
