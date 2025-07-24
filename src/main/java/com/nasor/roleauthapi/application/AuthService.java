package com.nasor.roleauthapi.application;

import com.nasor.roleauthapi.application.dto.AuthResponseDto;
import com.nasor.roleauthapi.application.dto.LoginRequestDto;
import com.nasor.roleauthapi.application.dto.RegisterRequestDto;

public interface AuthService {
    AuthResponseDto registerUser(RegisterRequestDto registerRequestDto);
    AuthResponseDto login(LoginRequestDto loginRequestDto);
    AuthResponseDto refreshToken(String refreshToken);
}
