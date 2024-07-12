package com.dera.AuthFortress.repository;

import com.dera.AuthFortress.domain.entities.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findByToken(String token);

    Optional<AccessToken> findByUserId(Long userId);
    Optional<AccessToken> findByLoginToken(String loginToken);

}
