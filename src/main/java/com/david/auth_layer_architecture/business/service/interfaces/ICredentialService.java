package com.david.auth_layer_architecture.business.service.interfaces;

import com.david.auth_layer_architecture.common.exceptions.accessToken.AlreadyHaveAccessTokenToChangePasswordException;
import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.request.ChangePasswordRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import jakarta.mail.MessagingException;

public interface ICredentialService {
    MessageResponse signUp(Credential credential, Boolean isAccessWithOAuth2) throws UserAlreadyExistException;

    SignInResponse verifyAccount(String accessTokenId);

    MessageResponse refreshAccessToVerifyAccount(Credential credential, String refreshToken);

    MessageResponse recoveryAccount(
            String email
    ) throws UserNotFoundException, HaveAccessWithOAuth2Exception, UserNotVerifiedException;

    MessageResponse changePassword(Credential credential, String password, String accessTokenId);

    Credential getCredentialByEmail(String email);

    Credential isRegisteredUser(String email) throws UserNotFoundException;

    void hasAccessWithOAuth2(Credential credential) throws HaveAccessWithOAuth2Exception;
}
