package com.david.auth_layer_architecture.business.facade.interfaces;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.domain.dto.request.ChangePasswordRequest;
import com.david.auth_layer_architecture.domain.dto.request.RecoveryAccountRequest;
import com.david.auth_layer_architecture.domain.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;

public interface ICredentialFacade {
   MessageResponse signUp(SignUpRequest signUpRequest) throws UserAlreadyExistException;

   SignInResponse verifyAccount(String accessTokenId);

   MessageResponse refreshAccessToVerifyAccount(Credential credential, String refreshToken);

   MessageResponse recoveryAccount(
           RecoveryAccountRequest recoveryAccountRequest
   ) throws UserNotFoundException, HaveAccessWithOAuth2Exception, UserNotVerifiedException;

   MessageResponse changePassword(Credential credential, ChangePasswordRequest changePasswordRequest, String accessTokenId);
}
