package com.david.auth_layer_architecture.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "MessageResponse",
        description = "Schema for a message response object"
)
public record MessageResponse(

        @Schema(
                description = "The message to be returned to the user"
        )
        String message

) { }