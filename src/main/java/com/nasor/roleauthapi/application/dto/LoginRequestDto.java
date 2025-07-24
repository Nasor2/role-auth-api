package com.nasor.roleauthapi.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Entry data to request User Log in.")
public record LoginRequestDto(
        @Schema(description = "Username for Log in.", example = "Reynaldo1987")
        @NotBlank(message = "Username is required.")
        String username,

        @Schema(description = "Password for Log in.", example = "SecurePass123!")
        @NotBlank(message = "Password is required")
        String password)
{
}
