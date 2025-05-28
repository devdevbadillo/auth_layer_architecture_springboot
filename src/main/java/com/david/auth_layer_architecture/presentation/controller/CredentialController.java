package com.david.auth_layer_architecture.presentation.controller;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.routes.CredentialRoutes;
import com.david.auth_layer_architecture.presentation.dto.request.ChangePasswordRequest;
import com.david.auth_layer_architecture.presentation.dto.request.RecoveryAccountRequest;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import com.david.auth_layer_architecture.domain.entity.Credential;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.david.auth_layer_architecture.domain.application.ICredentialFacade;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.presentation.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;

import jakarta.validation.Valid;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping(path = CommonConstants.PUBLIC_URL, produces = { MediaType.APPLICATION_JSON_VALUE })
public class CredentialController {
    private final ICredentialFacade credentialFacade;

    @PostMapping(CredentialRoutes.SIGN_UP_URL)
    public ResponseEntity<MessageResponse> signUp( @RequestBody @Valid SignUpRequest signUpRequest ) throws UserAlreadyExistException{
        return ResponseEntity.ok(credentialFacade.signUp(signUpRequest));
    }

    @PatchMapping(CredentialRoutes.VERIFY_ACCOUNT_URL)
    public ResponseEntity<PairTokenResponse> verifyAccount( HttpServletRequest request ){
        String accessTokenId = (String) request.getAttribute("accessTokenId");

        return ResponseEntity.ok(credentialFacade.verifyAccount(accessTokenId));
    }

    @PatchMapping(CredentialRoutes.REFRESH_ACCESS_TO_VERIFY_ACCOUNT_URL)
    public ResponseEntity<MessageResponse> refreshAccessToRecoveryAccount( HttpServletRequest request )  {
        Credential credential = (Credential) request.getAttribute("credential");
        String refreshToken = (String) request.getAttribute("refreshToken");

        return ResponseEntity.ok(credentialFacade.refreshAccessToVerifyAccount(credential, refreshToken));
    }

    @PostMapping(CredentialRoutes.RECOVERY_ACCOUNT_URL)
    public ResponseEntity<MessageResponse> recoveryAccount(
            @RequestBody @Valid RecoveryAccountRequest recoveryAccountRequest
    ) throws UserNotFoundException, UserNotVerifiedException, HaveAccessWithOAuth2Exception {
        return ResponseEntity.ok(credentialFacade.recoveryAccount(recoveryAccountRequest));
    }

    @GetMapping(CredentialRoutes.CHANGE_PASSWORD_URL)
    public ResponseEntity<MessageResponse> viewChangePassword() {
        return ResponseEntity.ok(new MessageResponse("Ok"));
    }

    @PatchMapping(CredentialRoutes.CHANGE_PASSWORD_URL)
    public ResponseEntity<MessageResponse> changePassword(
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request
    ){
        Credential credential =(Credential) request.getAttribute("credential");
        String accessTokenId = (String) request.getAttribute("accessTokenId");

        return ResponseEntity.ok(credentialFacade.changePassword(credential, changePasswordRequest, accessTokenId));
    }
}
