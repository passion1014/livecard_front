package com.livecard.front.common.service;

import com.nimbusds.oauth2.sdk.token.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken findByRefreshToken(String refreshToken);
}
