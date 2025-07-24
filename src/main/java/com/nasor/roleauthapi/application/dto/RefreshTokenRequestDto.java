package com.nasor.roleauthapi.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Entry data to request a Refresh Token.")
public record RefreshTokenRequestDto (
        @Schema(description = "Token previously given to generate new Access Tokens.", example = "e5f6a7b8-c9d0-1234-5678-90abcdef1234")
        @NotBlank(message = "Refresh token cannot be blank")
        String refreshToken)
{
}
