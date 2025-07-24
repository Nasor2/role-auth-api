package com.nasor.roleauthapi.application;

import com.nasor.roleauthapi.application.dto.RegisterRequestDto;
import com.nasor.roleauthapi.application.dto.UserResponseDto;
import com.nasor.roleauthapi.domain.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserResponseDto> findUserById(Long id);
    List<UserResponseDto> findAllUsers();
    UserResponseDto update(Long id, RegisterRequestDto registerRequestDto);
    UserResponseDto updateRole(Long id, Role role) ;
}
