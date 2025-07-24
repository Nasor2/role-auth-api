package com.nasor.roleauthapi.infraestructure.controller;

import com.nasor.roleauthapi.application.UserService;
import com.nasor.roleauthapi.application.dto.UserResponseDto;
import com.nasor.roleauthapi.domain.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Management", description = "Endpoints accessible only by users with ADMIN role.")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Allows an ADMIN to view the complete list of registered users in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required or invalid token.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have ADMIN role.",
                    content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Update a user's role", description = "Allows an ADMIN to change another user's role (e.g., from USER to ADMIN, or vice versa).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role updated successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid role specified.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required or invalid token.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have ADMIN role.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Not Found: User with specified ID not found.",
                    content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserResponseDto> updateUserRole(
            @Parameter(description = "ID of the user whose role will be updated.", required = true, example = "2")
            @PathVariable Long id,
            @RequestBody(description = "New role for the user (ADMIN or USER).", required = true,
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", allowableValues = {"ADMIN", "USER"}, example = "ADMIN")))
            @org.springframework.web.bind.annotation.RequestBody
            String newRole)
    {
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(newRole.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        UserResponseDto updatedUser = userService.updateRole(id, roleEnum);
        return ResponseEntity.ok(updatedUser);
    }
}