package com.david.auth_layer_architecture.business.facade.interfaces;

import com.david.auth_layer_architecture.common.exceptions.accessToken.AlreadyHaveAccessTokenToChangePasswordException;
import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.request.ChangePasswordRequest;
import com.david.auth_layer_architecture.domain.dto.request.RecoveryAccountRequest;
import com.david.auth_layer_architecture.domain.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import jakarta.mail.MessagingException;

public interface ICredentialFacade {
   MessageResponse signUp(SignUpRequest signUpRequest) throws UserAlreadyExistException, MessagingException;

   SignInResponse verifyAccount(String accessTokenId);

   MessageResponse recoveryAccount(
           RecoveryAccountRequest recoveryAccountRequest
   ) throws UserNotFoundException, HaveAccessWithOAuth2Exception, MessagingException, AlreadyHaveAccessTokenToChangePasswordException;

   MessageResponse changePassword(
           ChangePasswordRequest changePasswordRequest, String email, String accessTokenId
   ) throws HaveAccessWithOAuth2Exception, UserNotFoundException;
}
