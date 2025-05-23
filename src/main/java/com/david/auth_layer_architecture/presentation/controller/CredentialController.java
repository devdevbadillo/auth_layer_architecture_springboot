package com.david.auth_layer_architecture.presentation.controller;

import com.david.auth_layer_architecture.common.exceptions.auth.HaveAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.common.exceptions.auth.UserNotVerifiedException;
import com.david.auth_layer_architecture.common.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.routes.CredentialRoutes;
import com.david.auth_layer_architecture.domain.dto.request.ChangePasswordRequest;
import com.david.auth_layer_architecture.domain.dto.request.RecoveryAccountRequest;
import com.david.auth_layer_architecture.domain.dto.response.SignInResponse;
import com.david.auth_layer_architecture.domain.entity.AccessToken;
import com.david.auth_layer_architecture.domain.entity.Credential;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.david.auth_layer_architecture.business.facade.interfaces.ICredentialFacade;
import com.david.auth_layer_architecture.common.exceptions.credential.UserAlreadyExistException;
import com.david.auth_layer_architecture.domain.dto.request.SignUpRequest;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

import jakarta.validation.Valid;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping(path = CommonConstants.PUBLIC_URL, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @PostMapping(CredentialRoutes.SIGN_UP_URL)
    public ResponseEntity<MessageResponse> signUp(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) throws UserAlreadyExistException{
        return ResponseEntity.ok(credentialFacade.signUp(signUpRequest));
    }

    @Operation(
            summary = "Verify account",
            description = "Verify account with access token in header Authorization Bearer token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Account verified successfully\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Access denied\"}"
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
    @PatchMapping(CredentialRoutes.VERIFY_ACCOUNT_URL)
    public ResponseEntity<SignInResponse> verifyAccount(
            HttpServletRequest request
    ){
        String accessTokenId = (String) request.getAttribute("accessTokenId");

        return ResponseEntity.ok(credentialFacade.verifyAccount(accessTokenId));
    }

    @Operation(
            summary = "Refresh access to verify account",
            description = "Refresh access to verify account with refresh token in header Authorization Bearer token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Instructions to verify account sent successfully, check your email\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Access denied\"}"
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
    @PatchMapping(CredentialRoutes.REFRESH_ACCESS_TO_VERIFY_ACCOUNT_URL)
    public ResponseEntity<MessageResponse> refreshAccessToRecoveryAccount(
            HttpServletRequest request
    )  {
        Credential credential = (Credential) request.getAttribute("credential");
        String refreshToken = (String) request.getAttribute("refreshToken");

        return ResponseEntity.ok(credentialFacade.refreshAccessToVerifyAccount(credential, refreshToken));
    }

    @Operation(
            summary = "Recovery account",
            description = "Send a recovery email to the user's email address and return a success message"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Account recovery instructions have been sent, please check your email.\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"The email you entered are incorrect, please try again\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not registered",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"User not registered\"}"
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
    @PostMapping(CredentialRoutes.RECOVERY_ACCOUNT_URL)
    public ResponseEntity<MessageResponse> recoveryAccount(
            @RequestBody @Valid RecoveryAccountRequest recoveryAccountRequest
    ) throws UserNotFoundException, UserNotVerifiedException, HaveAccessWithOAuth2Exception {
        return ResponseEntity.ok(credentialFacade.recoveryAccount(recoveryAccountRequest));
    }

    @Operation(
            summary = "View change password",
            description = "Endpoint for authorized users with access token in header Authorization Bearer token to change password"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Ok\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Access denied\"}"
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
    @GetMapping(CredentialRoutes.CHANGE_PASSWORD_URL)
    public ResponseEntity<MessageResponse> viewChangePassword() {
        return ResponseEntity.ok(new MessageResponse("Ok"));
    }

    @Operation(
            summary = "Change password",
            description = "Change the password of the user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Password changed successfully\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Password and repeat password do not match\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Access denied\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Internal server error\"}"            )
                    )
            )
    })
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
