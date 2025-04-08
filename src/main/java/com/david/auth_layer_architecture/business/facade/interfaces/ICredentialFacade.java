package com.david.auth_layer_architecture.business.facade.interfaces;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.request.ChangePasswordRequest;
import com.david.auth_layer_architecture.domain.dto.request.RecoveryAccountRequest;
import com.david.auth_layer_architecture.domain.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import jakarta.mail.MessagingException;

public interface ICredentialFacade {
   MessageResponse signUp(SignUpRequest signUpRequest) throws UserAlreadyExistException;

   MessageResponse recoveryAccount(RecoveryAccountRequest recoveryAccountRequest) throws UserNotFoundException, HaveAccessWithOAuth2Exception, MessagingException;

   MessageResponse changePassword(ChangePasswordRequest changePasswordRequest, String email) throws HaveAccessWithOAuth2Exception, UserNotFoundException;
}
