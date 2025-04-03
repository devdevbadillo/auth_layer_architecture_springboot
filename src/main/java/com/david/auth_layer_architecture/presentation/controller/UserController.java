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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.auth_layer_architecture.common.utils.constants.ApiConstants;
import com.david.auth_layer_architecture.domain.dto.response.MessageResponse;

@RestController
@RequestMapping(path = ApiConstants.SECURE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
        name = "User",
        description = "Endpoint for authenticated users"
)
public class UserController {

    @Operation(
            summary = "Sign in",
            description = "Authenticate a user and return the access token"
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
}
