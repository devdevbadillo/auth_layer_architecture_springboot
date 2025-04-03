package com.david.auth_layer_architecture.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.auth_layer_architecture.business.facade.interfaces.IAuthFacade;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.common.utils.constants.ApiConstants;
import com.david.auth_layer_architecture.domain.dto.request.SignInRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

import jakarta.validation.Valid;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping(path=  ApiConstants.PUBLIC_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(
    name = "Authentication",
    description = "Authentication endpoint"
)
public class AuthController {

    private final IAuthFacade authFacade;

    @Operation(
            summary = "Sign in",
            description = "Authenticate a user and return the access token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"*****\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Bad credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Email or password is incorrect\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Email is required\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Internal server error\"}"
                            )
                    )
            )

    })
    @PostMapping(ApiConstants.SIGNIN_URL)
    public ResponseEntity<MessageResponse> signIn(
            @RequestBody @Valid SignInRequest signInRequest
    ) throws BadCredentialsException, UserNotFoundException {
        return ResponseEntity.ok(authFacade.signIn(signInRequest));
    }

}