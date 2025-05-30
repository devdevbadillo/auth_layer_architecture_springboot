package com.david.auth_layer_architecture.application.facade.interfaces;

import com.david.auth_layer_architecture.domain.exceptions.auth.HasAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotVerifiedException;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotFoundException;
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
   ) throws UserNotFoundException, HasAccessWithOAuth2Exception, UserNotVerifiedException;

   MessageResponse changePassword(Credential credential, ChangePasswordRequest changePasswordRequest, String accessTokenId);
}
