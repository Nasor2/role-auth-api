package com.nasor.roleauthapi.infraestructure.service;

import com.nasor.roleauthapi.application.AuthService;
import com.nasor.roleauthapi.application.dto.AuthResponseDto;
import com.nasor.roleauthapi.application.dto.LoginRequestDto;
import com.nasor.roleauthapi.application.dto.RegisterRequestDto;
import com.nasor.roleauthapi.domain.*;
import com.nasor.roleauthapi.infraestructure.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AuthResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByUsername(registerRequestDto.username())){
            throw new RuntimeException("Username is already in use");
        }

        User newUser = User.builder()
                .username(registerRequestDto.username())
                .password(passwordEncoder.encode(registerRequestDto.password()))
                .firstName(registerRequestDto.firstName())
                .lastName(registerRequestDto.lastName())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(newUser);


        String refreshToken = createOrUpdateRefreshToken(savedUser).getToken();
        String accessToken = jwtService.generateToken(savedUser.getUsername(), savedUser.getRole().name());

        return new AuthResponseDto(accessToken, refreshToken, jwtService.getJwtExpiration()/1000L);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        System.out.println("DEBUG: Login attempt for username: " + loginRequestDto.username());

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.username(),
                            loginRequestDto.password()
                    )
            );
        } catch (Exception e){
            System.err.println("DEBUG: Authentication failed for " + loginRequestDto.username() + ": " + e.getMessage());
            throw new RuntimeException("Invalid username and password");
        }

        User user = userRepository.findByUsername(loginRequestDto.username())
                .orElseThrow(() -> new RuntimeException("Username not found"));


        refreshTokenRepository.deleteByUserId(user.getId());

        String accessToken = jwtService.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = createOrUpdateRefreshToken(user).getToken();

        return new AuthResponseDto(accessToken, refreshToken, jwtService.getJwtExpiration()/1000L);
    }

    @Override
    @Transactional
    public AuthResponseDto refreshToken(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(user.getUsername(), user.getRole().name());

        String newRefreshToken = createOrUpdateRefreshToken(user).getToken();

        return new AuthResponseDto(newAccessToken, newRefreshToken, jwtService.getJwtExpiration()/1000L);
    }

    private RefreshToken createOrUpdateRefreshToken(User user) {
        Optional<RefreshToken> existingRefreshTokenOptional = refreshTokenRepository.findByUserId(user.getId());

        RefreshToken refreshToken;
        if (existingRefreshTokenOptional.isPresent()) {
            refreshToken = existingRefreshTokenOptional.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiresAt(Instant.now().plusMillis(jwtService.getRefreshExpiration()));
        } else {
            refreshToken = RefreshToken.builder()
                    .user(user)
                    .expiresAt(Instant.now().plusMillis(jwtService.getRefreshExpiration()))
                    .token(UUID.randomUUID().toString())
                    .build();
        }

        return refreshTokenRepository.save(refreshToken);
    }
}
