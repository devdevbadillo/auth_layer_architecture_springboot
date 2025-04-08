package com.david.auth_layer_architecture.business.service.interfaces;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;
import jakarta.mail.MessagingException;

public interface ICredentialService {
    MessageResponse signUp(Credential credential) throws UserAlreadyExistException;

    MessageResponse recoveryAccount(String email) throws UserNotFoundException, HaveAccessWithOAuth2Exception, MessagingException;

    MessageResponse changePassword(String password, String repeatPassword);
}
