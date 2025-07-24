package com.nasor.roleauthapi.infraestructure.controller;

import com.nasor.roleauthapi.application.UserService;
import com.nasor.roleauthapi.application.dto.RegisterRequestDto;
import com.nasor.roleauthapi.application.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "User Management", description = "Endpoints for user-specific operations. Requires authentication.")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user by ID", description = "Retrieves details for a specific user. Accessible by ADMIN or the user itself.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required or invalid token.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have ADMIN role and is not the requested user.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Not Found: User with specified ID not found.",
                    content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "ID of the user to retrieve.", required = true, example = "1")
            @PathVariable Long id)
    {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user details", description = "Updates details for a specific user. Accessible by ADMIN or the user itself.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details updated successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid update data provided.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required or invalid token.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have ADMIN role and is not the requested user.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Not Found: User with specified ID not found.",
                    content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(description = "ID of the user to update.", required = true, example = "1")
            @PathVariable Long id,
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User update data.", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterRequestDto.class)))
            RegisterRequestDto request)
    {
        UserResponseDto updatedUser = userService.update(id, request);
        return ResponseEntity.ok(updatedUser);
    }
}