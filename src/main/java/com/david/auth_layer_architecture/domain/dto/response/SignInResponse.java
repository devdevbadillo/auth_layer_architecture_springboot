package com.david.auth_layer_architecture.domain.dto.response;

public record SignInResponse (
        String accessToken,

        String refreshToken ) {
}
