package com.livecard.front.common.service;


import com.livecard.front.domain.entity.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken findByRefreshToken(String refreshToken);
}
