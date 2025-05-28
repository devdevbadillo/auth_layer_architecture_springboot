package com.david.auth_layer_architecture.presentation.dto.response;

public record PairTokenResponse(
        String accessToken,

        String refreshToken ) {
}
