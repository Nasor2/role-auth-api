package com.nasor.roleauthapi.infraestructure.repository;

import com.nasor.roleauthapi.domain.RefreshToken;
import com.nasor.roleauthapi.domain.RefreshTokenRepository;
import com.nasor.roleauthapi.domain.User;
import com.nasor.roleauthapi.infraestructure.persistence.RefreshTokenEntity;
import com.nasor.roleauthapi.infraestructure.persistence.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final SpringDataRefreshTokenRepository springDataRefreshTokenRepository;
    private final SpringDataUserRepository springDataUserRepository;
    public RefreshTokenRepositoryImpl(SpringDataRefreshTokenRepository springDataRefreshTokenRepository,
                                      SpringDataUserRepository springDataUserRepository) {
        this.springDataRefreshTokenRepository = springDataRefreshTokenRepository;
        this.springDataUserRepository = springDataUserRepository;
    }

    private RefreshToken toDomainRefreshToken(RefreshTokenEntity domainRefreshTokenEntity) {
        if (domainRefreshTokenEntity == null) {
            return null;
        }
        return RefreshToken.builder()
                .id(domainRefreshTokenEntity.getId())
                .token(domainRefreshTokenEntity.getToken())
                .user(User.builder()
                        .id(domainRefreshTokenEntity.getUser().getId())
                        .username(domainRefreshTokenEntity.getUser().getUsername())
                        .role(domainRefreshTokenEntity.getUser().getRole())
                        .firstName(domainRefreshTokenEntity.getUser().getFirstName())
                        .lastName(domainRefreshTokenEntity.getUser().getLastName())
                        .password(domainRefreshTokenEntity.getUser().getPassword())
                        .build())
                .expiresAt(domainRefreshTokenEntity.getExpiryDate())
                .build();
    }

    private RefreshTokenEntity toJpaRefreshTokenEntity(RefreshToken domainRefreshTokenEntity) {
        if (domainRefreshTokenEntity == null) {
            return null;
        }
        UserEntity userEntity = springDataUserRepository.findByUsername(domainRefreshTokenEntity.getUser().getUsername())
                .orElse(null);
        return RefreshTokenEntity.builder()
                .id(domainRefreshTokenEntity.getId())
                .token(domainRefreshTokenEntity.getToken())
                .user(userEntity)
                .expiryDate(domainRefreshTokenEntity.getExpiresAt())
                .build();
    }


    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity savedEntity = springDataRefreshTokenRepository.save(toJpaRefreshTokenEntity(refreshToken));
        return toDomainRefreshToken(savedEntity);
    }

    @Override
    public void delete(RefreshToken refreshToken) {
        springDataRefreshTokenRepository.delete(toJpaRefreshTokenEntity(refreshToken));
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        springDataRefreshTokenRepository.findById(userId).ifPresent(springDataRefreshTokenRepository::delete);
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return springDataRefreshTokenRepository.findByUser_Id(userId).map(this::toDomainRefreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return springDataRefreshTokenRepository.findByToken(token).map(this::toDomainRefreshToken);
    }


}
