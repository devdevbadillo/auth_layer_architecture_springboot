package com.david.auth_layer_architecture.presentation.controller;

import com.david.auth_layer_architecture.business.facade.interfaces.IUserFacade;
import com.david.auth_layer_architecture.common.utils.constants.CommonConstants;
import com.david.auth_layer_architecture.common.utils.constants.routes.CredentialRoutes;
import com.david.auth_layer_architecture.common.utils.constants.routes.UserRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

@RestController
@RequestMapping(path = CommonConstants.SECURE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
        name = "User",
        description = "Endpoint for authenticated users"
)
public class UserController {

    private final IUserFacade userFacade;

    public UserController(IUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Operation(
            summary = "Private endpoint",
            description = "Endpoint for authenticated users with access token in header Authorization Bearer token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authorization successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"welcome!\"}"
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
    @GetMapping("/user")
    public ResponseEntity<MessageResponse> home() {
        return ResponseEntity.ok(new MessageResponse("welcome!"));
    }

    @Operation(
            summary = "Sign out",
            description = "Endpoint for authenticated users with access token in header Authorization Bearer token to sign out"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sign out successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"message\": \"Sign out successful\"}"
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
    @PostMapping(UserRoutes.SIGN_OUT_URL)
    public ResponseEntity<MessageResponse> signOut(
            HttpServletRequest request
    ) {
        String accessTokenId =(String) request.getAttribute("accessTokenId");
        return ResponseEntity.ok(this.userFacade.signOut(accessTokenId));
    }
}
