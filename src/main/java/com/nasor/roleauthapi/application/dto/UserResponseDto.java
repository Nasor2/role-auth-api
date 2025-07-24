package com.nasor.roleauthapi.application.dto;

import com.nasor.roleauthapi.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User data representation.")
public record UserResponseDto(
        @Schema(description = "User ID", example = "12")
        Long id,

        @Schema(description = "User username", example = "Paolo123M")
        String username,

        @Schema(description = "User full name",example = "Paolo Fernandez")
        String fullName,

        @Schema(description = "User role in system.", example = "USER")
        Role role
) {
}
