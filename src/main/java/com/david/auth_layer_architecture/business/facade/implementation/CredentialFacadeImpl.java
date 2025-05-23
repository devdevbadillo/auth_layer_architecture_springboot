package com.david.auth_layer_architecture.business.facade.implementation;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.common.mapper.CredentialEntityMapper;
import com.david.auth_layer_architecture.domain.dto.request.ChangePasswordRequest;
import com.david.auth_layer_architecture.domain.dto.request.RecoveryAccountRequest;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import lombok.AllArgsConstructor;
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
    private final CredentialEntityMapper credentialEntityMapper;

    @Override
    public MessageResponse signUp(SignUpRequest signUpRequest) throws UserAlreadyExistException {
        Credential credential = this.credentialEntityMapper.toCredentialEntity(signUpRequest);
        return credentialService.signUp(credential, false);
    }

    @Override
    public SignInResponse verifyAccount(String accessTokenId) {
        return this.credentialService.verifyAccount(accessTokenId);
    }

    @Override
    public MessageResponse refreshAccessToVerifyAccount(Credential credential, String refreshToken){
        return this.credentialService.refreshAccessToVerifyAccount(credential, refreshToken);
    }

    @Override
    public MessageResponse recoveryAccount(
            RecoveryAccountRequest recoveryAccountRequest
    ) throws UserNotFoundException, HaveAccessWithOAuth2Exception, UserNotVerifiedException {
        return this.credentialService.recoveryAccount(recoveryAccountRequest.getEmail());
    }

    @Override
    public MessageResponse changePassword(Credential credential, ChangePasswordRequest changePasswordRequest, String accessTokenId){
        String passwordHash = this.credentialEntityMapper.encodePassword(changePasswordRequest.getPassword());
        return this.credentialService.changePassword(credential, passwordHash, accessTokenId);
    }

}
