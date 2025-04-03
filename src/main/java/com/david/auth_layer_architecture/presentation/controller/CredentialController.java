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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.auth_layer_architecture.business.facade.interfaces.ICredentialFacade;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.common.utils.constants.ApiConstants;
import com.david.auth_layer_architecture.domain.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

import jakarta.validation.Valid;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping(path = ApiConstants.PUBLIC_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
        name = "Credential",
        description = "Credential API to sign up, account recovery and change password"
)
public class CredentialController {

    private final ICredentialFacade credentialFacade;

    @Operation(
            summary = "Sign up a new user",
            description = "Sign up a new user with email and password, send a confirmation email to the user's email address and return a success message"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"User created successfully\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"User already exists\"}"
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
    @PostMapping(ApiConstants.SIGNUP_URL)
    public ResponseEntity<MessageResponse> signUp(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) throws UserAlreadyExistException{
        return ResponseEntity.ok(credentialFacade.signUp(signUpRequest));
    }
}
