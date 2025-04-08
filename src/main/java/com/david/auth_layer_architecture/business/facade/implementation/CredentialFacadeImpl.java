package com.david.auth_layer_architecture.business.facade.implementation;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.request.ChangePasswordRequest;
import com.david.auth_layer_architecture.domain.dto.request.RecoveryAccountRequest;
import jakarta.mail.MessagingException;
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

    @Override
    public MessageResponse recoveryAccount(RecoveryAccountRequest recoveryAccountRequest) throws UserNotFoundException, HaveAccessWithOAuth2Exception, MessagingException {
        return this.credentialService.recoveryAccount(recoveryAccountRequest.getEmail());
    }

    @Override
    public MessageResponse changePassword(ChangePasswordRequest changePasswordRequest, String email) throws HaveAccessWithOAuth2Exception, UserNotFoundException {
        return this.credentialService.changePassword(encodePassword(changePasswordRequest.getPassword()), email);
    }

    private Credential buildCredential(SignUpRequest signUpRequest){
        return Credential.builder()
                .email(signUpRequest.getEmail())
                .password(encodePassword(signUpRequest.getPassword()))
                .name(signUpRequest.getName())
                .isAccesOauth(false)
                .build();
    }

    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
