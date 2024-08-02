package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OauthToken extends SystemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "MBR_USER_ID", nullable = false, unique = true)
    private Long mbrUserId;

    @Column(name = "PROVIDER_CD", length = 1, nullable = false)
    private String providerCd; // 제공자 (KAKAO=0, NAVER=1, GOOGLE=2)

    @Column(name = "REFRESH_TOKEN", nullable = false)
    private String refreshToken;

    @Column(name = "PROVIDER_ACCESS_TOKEN", nullable = false)
    private String providerAccessToken;

    @Column(name = "PROVIDER_REFRESH_TOKEN", nullable = false)
    private String providerRefreshToken;

    public OauthToken(Long mbrUserId, String providerCd, String refreshToken, String providerAccessToken, String providerRefreshToken) {
        this.mbrUserId = mbrUserId;
        this.providerCd = providerCd;
        this.refreshToken = refreshToken;
        this.providerAccessToken = providerAccessToken;
        this.providerRefreshToken = providerRefreshToken;
    }

    public OauthToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }

    public OauthToken update(String providerAccessToken, String providerRefreshToken) {
        this.providerAccessToken = providerAccessToken;
        this.providerRefreshToken = providerRefreshToken;
        return this;
    }

    public OauthToken update(String refreshToken, String providerAccessToken, String providerRefreshToken) {
        this.refreshToken = refreshToken;
        this.providerAccessToken = providerAccessToken;
        this.providerRefreshToken = providerRefreshToken;
        return this;
    }
}

