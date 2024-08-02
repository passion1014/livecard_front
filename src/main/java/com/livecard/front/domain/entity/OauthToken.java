package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OauthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "MBR_USER_ID", nullable = false, unique = true)
    private Long mbrUserId;

    @Column(name = "PROVIDER_CD", length = 1, nullable = false)
    private String providerCd; // 제공자 (KAKAO=0, NAVER=1, GOOGLE=2)

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public OauthToken(Long mbrUserId, String providerCd, String accessToken, String refreshToken) {
        this.mbrUserId = mbrUserId;
        this.providerCd = providerCd;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public OauthToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }

    public OauthToken update(String newAccessToken, String newRefreshToken) {
        this.accessToken = newAccessToken;
        return this;
    }
}

