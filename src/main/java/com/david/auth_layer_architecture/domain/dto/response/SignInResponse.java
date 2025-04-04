package com.david.auth_layer_architecture.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignInResponse {

    private  String accessToken;
    private  String refreshToken;
}
