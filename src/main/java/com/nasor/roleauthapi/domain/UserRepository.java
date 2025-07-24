package com.nasor.roleauthapi.domain;


import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    User save(User user);
    boolean existsByUsername(String username);
    List<User> findAllUsers();
}
