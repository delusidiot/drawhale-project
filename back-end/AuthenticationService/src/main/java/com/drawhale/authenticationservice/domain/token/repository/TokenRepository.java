package com.drawhale.authenticationservice.domain.token.repository;

import com.drawhale.authenticationservice.domain.token.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Query("""
    SELECT t FROM TokenEntity t INNER JOIN UserEntity u ON t.user.id = u.id
    WHERE u.id = :userId AND (t.expired = false or t.revoked = false)
    """)
    List<TokenEntity> findAllValidTokensByUser(Long userId);

    Optional<TokenEntity> findByRefreshToken(String token);
}
