package com.nasor.roleauthapi.infraestructure.repository;

import com.nasor.roleauthapi.infraestructure.persistence.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String Token);
    Optional<RefreshTokenEntity> findByUser_Id(Long userId);
}
