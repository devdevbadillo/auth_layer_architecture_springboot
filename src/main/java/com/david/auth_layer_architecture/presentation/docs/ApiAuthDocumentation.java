package com.david.auth_layer_architecture.presentation.docs;

import com.david.auth_layer_architecture.domain.exceptions.auth.HasAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotVerifiedException;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.presentation.dto.request.SignInRequest;
import com.david.auth_layer_architecture.presentation.dto.response.MessageResponse;
import com.david.auth_layer_architecture.presentation.dto.response.PairTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(
        name = "Authentication",
        description = "Authentication endpoint"
)
public interface ApiAuthDocumentation {

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
    ResponseEntity<PairTokenResponse> signIn(
            @RequestBody @Valid SignInRequest signInRequest
    ) throws UserNotFoundException, BadCredentialsException, HasAccessWithOAuth2Exception, UserNotVerifiedException;


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
    ResponseEntity<PairTokenResponse> refreshToken(@RequestHeader @NotBlank @NotNull String refreshToken);

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
    ResponseEntity<MessageResponse> authenticationOAuth2Error();
}
