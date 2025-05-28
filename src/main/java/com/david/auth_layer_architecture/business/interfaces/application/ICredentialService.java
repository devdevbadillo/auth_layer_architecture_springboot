package com.david.auth_layer_architecture.business.interfaces.application;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;

public interface ICredentialService {
    MessageResponse signUp(Credential credential, Boolean isAccessWithOAuth2) throws UserAlreadyExistException;

    PairTokenResponse verifyAccount(String accessTokenId);

    MessageResponse refreshAccessToVerifyAccount(Credential credential, String refreshToken);

    MessageResponse recoveryAccount(
            String email
    ) throws UserNotFoundException, HaveAccessWithOAuth2Exception, UserNotVerifiedException;

    MessageResponse changePassword(Credential credential, String password, String accessTokenId);

    Credential getCredentialByEmail(String email);

    Credential isRegisteredUser(String email) throws UserNotFoundException;

    void hasAccessWithOAuth2(Credential credential) throws HaveAccessWithOAuth2Exception;
}
