package com.nasor.roleauthapi.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Entry data to request the Register of a new user.")
public record RegisterRequestDto(

        @Schema(description = "Unique Username.", example = "Paolo1284")
        @NotBlank(message = "Username is required.")
        String username,

        @Schema(description = "User's firstname.", example = "Paolo")
        @NotBlank(message = "First Name is required.")
        String firstName,

        @Schema(description = "User's lastname.", example = "Fernandez")
        @NotBlank(message = "Last Name is required.")
        String lastName,

        @Schema(description = "User's password.", example = "MyPassWord123$")
        @NotBlank(message = "Password is required.")
        String password
)
{
}
