package com.nasor.roleauthapi.infraestructure.repository;

import com.nasor.roleauthapi.domain.User;
import com.nasor.roleauthapi.domain.UserRepository;
import com.nasor.roleauthapi.infraestructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final SpringDataUserRepository springDataUserRepository;

    public UserRepositoryImpl(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    private User toDomainUser(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
    }

    private UserEntity toUserEntity(User domainUser) {
        if (domainUser == null) {
            return null;
        }
        return UserEntity.builder()
                .id(domainUser.getId())
                .username(domainUser.getUsername())
                .password(domainUser.getPassword())
                .role(domainUser.getRole())
                .firstName(domainUser.getFirstName())
                .lastName(domainUser.getLastName())
                .build();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springDataUserRepository.findByUsername(username).map(this::toDomainUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return springDataUserRepository.findById(id).map(this::toDomainUser);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = springDataUserRepository.save(toUserEntity(user));
        return toDomainUser(userEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return springDataUserRepository.existsByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return springDataUserRepository.findAll().stream().map(this::toDomainUser).collect(Collectors.toList());
    }
}
