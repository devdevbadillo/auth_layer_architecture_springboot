package com.david.auth_layer_architecture.presentation.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.routes.AuthRoutes;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.david.auth_layer_architecture.business.facade.interfaces.IAuthFacade;
import com.david.auth_layer_architecture.domain.dto.request.SignInRequest;

import jakarta.validation.Valid;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping(path=  CommonConstants.PUBLIC_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
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
                                    example = "{\"token\": \"*****\", \"refreshToken\": \"*****\"}"
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
    @PostMapping(value = AuthRoutes.SIGN_IN_URL, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SignInResponse> signIn(
            @RequestBody @Valid SignInRequest signInRequest
    ) throws UserNotFoundException, BadCredentialsException, HaveAccessWithOAuth2Exception, UserNotVerifiedException {
        return ResponseEntity.ok(authFacade.signIn(signInRequest));
    }

    @Operation(
            summary = "Refresh access token",
            description = "Endpoint to refresh the access token, send the refresh token in the header 'refreshToken'"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Valid token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"token\": \"*****\", \"refreshToken\": \"*****\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Invalid token\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"refresh token is required\"}"
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
    @PostMapping(value = AuthRoutes.REFRESH_TOKEN_URL, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SignInResponse> refreshToken(
            @RequestHeader @NotBlank @NotNull String refreshToken
    ) {
        return ResponseEntity.ok(authFacade.refreshToken(refreshToken));
    }

    @Operation(
            summary = "OAuth2 error",
            description = "Endpoint for OAuth2 error",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Authentication error\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Invalid token\"}"
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
            }
    )
    @GetMapping(AuthRoutes.OAUTH2_ERROR_URL)
    public ResponseEntity<MessageResponse> authenticationOAuth2Error() {
        return ResponseEntity.ok(new MessageResponse("Authentication error"));
    }
}