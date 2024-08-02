package com.livecard.front.domain.repository;



import com.livecard.front.domain.entity.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<OauthToken, Long> {
    Optional<OauthToken> findByRefreshToken(String refreshToken);

    Optional<OauthToken> findByAccessToken(String accessToken);

    Optional<OauthToken> findByMbrUserIdAndProviderCd(Long mbrUserId, String providerCd);

    void deleteByMbrUserId(Long mbrUserId);

}

