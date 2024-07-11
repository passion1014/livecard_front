package com.livecard.front.domain.repository;



import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByMbrUserId(Long mbrUserId);

}

