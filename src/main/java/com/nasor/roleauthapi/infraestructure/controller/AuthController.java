package com.nasor.roleauthapi.infraestructure.controller;

import com.nasor.roleauthapi.application.AuthService;
import com.nasor.roleauthapi.application.dto.AuthResponseDto;
import com.nasor.roleauthapi.application.dto.LoginRequestDto;
import com.nasor.roleauthapi.application.dto.RefreshTokenRequestDto;
import com.nasor.roleauthapi.application.dto.RegisterRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration, login, and token refresh.")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "Allows a new user to register in the system with a default role (USER).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully and tokens generated.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid registration data provided.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "409", description = "Username is already in use.",
                    content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("/register")
    @SecurityRequirements
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "User login", description = "Allows a user to authenticate and obtain an Access Token and a Refresh Token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful and tokens generated.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials (username or password).",
                    content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("/login")
    @SecurityRequirements
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        AuthResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh Access Token", description = "Uses a Refresh Token to obtain a new Access Token and a new Refresh Token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token request (e.g., blank token).",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "401", description = "Refresh Token not found or invalid.",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Refresh Token expired.",
                    content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("/refresh-token")
    @SecurityRequirements
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {
        AuthResponseDto response = authService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }
}