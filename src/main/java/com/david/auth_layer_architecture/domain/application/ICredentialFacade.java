package com.david.auth_layer_architecture.domain.application;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.presentation.dto.request.ChangePasswordRequest;
import com.david.auth_layer_architecture.presentation.dto.request.RecoveryAccountRequest;
import com.david.auth_layer_architecture.presentation.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;

public interface ICredentialFacade {
   MessageResponse signUp(SignUpRequest signUpRequest) throws UserAlreadyExistException;

   PairTokenResponse verifyAccount(String accessTokenId);

   MessageResponse refreshAccessToVerifyAccount(Credential credential, String refreshToken);

   MessageResponse recoveryAccount(
           RecoveryAccountRequest recoveryAccountRequest
   ) throws UserNotFoundException, HaveAccessWithOAuth2Exception, UserNotVerifiedException;

   MessageResponse changePassword(Credential credential, ChangePasswordRequest changePasswordRequest, String accessTokenId);
}
