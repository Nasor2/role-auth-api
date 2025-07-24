package com.nasor.roleauthapi.infraestructure.service;

import com.nasor.roleauthapi.application.UserService;
import com.nasor.roleauthapi.application.dto.RegisterRequestDto;
import com.nasor.roleauthapi.application.dto.UserResponseDto;
import com.nasor.roleauthapi.domain.Role;
import com.nasor.roleauthapi.domain.User;
import com.nasor.roleauthapi.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserResponseDto toUserResponseDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDto(user.getId(),
                user.getUsername(),
                user.getFirstName() + " " +  user.getLastName(),
                user.getRole()
        );
    }

    @Override
    public Optional<UserResponseDto> findUserById(Long id) {
        return userRepository.findById(id).map(this::toUserResponseDto);
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAllUsers()
                .stream()
                .map(this::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, RegisterRequestDto registerRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (registerRequestDto.username() != null && !registerRequestDto.username().isEmpty()) {
            existingUser.setUsername(registerRequestDto.username());
        }
        if (registerRequestDto.password() != null && !registerRequestDto.password().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(registerRequestDto.password()));
        }
        if (registerRequestDto.firstName() != null && !registerRequestDto.firstName().isEmpty()) {
            existingUser.setFirstName(registerRequestDto.firstName());
        }
        if (registerRequestDto.lastName() != null && !registerRequestDto.lastName().isEmpty()) {
            existingUser.setLastName(registerRequestDto.lastName());
        }

        User updatedUser = userRepository.save(existingUser);
        return toUserResponseDto(updatedUser);
    }

    @Override
    public UserResponseDto updateRole(Long id, Role role) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        userToUpdate.setRole(role);
        userRepository.save(userToUpdate);
        return toUserResponseDto(userToUpdate);
    }
}
