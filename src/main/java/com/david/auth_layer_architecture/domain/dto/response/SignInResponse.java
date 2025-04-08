package com.david.auth_layer_architecture.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "SignInResponse",
        description = "Schema for the response of the sign in and refresh token endpoint"
)
public class SignInResponse {

    @Schema(
            description = "The access token to be returned to the user"
    )
    private  String accessToken;

    @Schema(
            description = "The refresh token to be returned to the user"
    )
    private  String refreshToken;
}
