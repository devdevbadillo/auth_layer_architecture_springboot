package com.david.auth_layer_architecture.presentation.controller;

import com.david.auth_layer_architecture.domain.exceptions.auth.HasAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotVerifiedException;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.infrestructure.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.infrestructure.utils.constants.routes.AuthRoutes;
import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.david.auth_layer_architecture.application.facade.interfaces.IAuthFacade;
import com.david.auth_layer_architecture.presentation.dto.request.SignInRequest;

import jakarta.validation.Valid;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping(path=  CommonConstants.PUBLIC_URL, produces = { MediaType.APPLICATION_JSON_VALUE })
public class AuthController {
    private final IAuthFacade authFacade;

    @PostMapping(value = AuthRoutes.SIGN_IN_URL, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PairTokenResponse> signIn(
            @RequestBody @Valid SignInRequest signInRequest
    ) throws UserNotFoundException, BadCredentialsException, HasAccessWithOAuth2Exception, UserNotVerifiedException {
        return ResponseEntity.ok(authFacade.signIn(signInRequest));
    }

    @PostMapping(value = AuthRoutes.REFRESH_TOKEN_URL, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PairTokenResponse> refreshToken( @RequestHeader @NotBlank @NotNull String refreshToken ) {
        return ResponseEntity.ok(authFacade.refreshToken(refreshToken));
    }

    @GetMapping(AuthRoutes.OAUTH2_ERROR_URL)
    public ResponseEntity<MessageResponse> authenticationOAuth2Error() {
        return ResponseEntity.ok(new MessageResponse("Authentication error"));
    }
}