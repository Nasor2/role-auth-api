package com.nasor.roleauthapi.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication Response, contains Access Token, Refresh Token and Details")
public record AuthResponseDto(
        @Schema(description = "Access JWT Token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsInJvbG.123abc...")
        String accessToken,

        @Schema(description = "Refresh Token, can be used to obtain new Access Tokens.", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
        String refreshToken,

        @Schema(description = "Token Type (ALWAYS 'Bearer').", example = "Bearer")
        String tokenType,

        @Schema(description = "Access Token expiration Time in Seconds.", example = "3600")
        Long expiresIn
) {
    public AuthResponseDto(String accessToken,
                           String refreshToken,
                           Long expiresIn)
    {
        this(accessToken, refreshToken, "Bearer", expiresIn);
    }
}
