package com.nasor.roleauthapi.domain;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken save(RefreshToken refreshToken);
    void delete(RefreshToken refreshToken);
    void deleteByUserId(Long userId);
    Optional<RefreshToken> findByUserId(Long userId);
}
