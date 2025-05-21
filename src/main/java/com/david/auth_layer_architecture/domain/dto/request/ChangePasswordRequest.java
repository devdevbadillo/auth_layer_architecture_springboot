package com.david.auth_layer_architecture.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordRequest {
    @NotBlank
    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 20)
    private String repeatPassword;
}
