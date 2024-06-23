package com.livecard.front.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "MBR_USER_ID", nullable = false, unique = true)
    private Long mbrUserId;


    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public RefreshToken(Long mbrUserId, String refreshToken) {
        this.mbrUserId = mbrUserId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;

        return this;
    }
}

