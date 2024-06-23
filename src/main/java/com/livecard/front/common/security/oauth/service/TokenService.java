package com.livecard.front.common.security.oauth.service;


import com.livecard.front.common.security.oauth.TokenProvider;
import com.livecard.front.common.service.RefreshTokenService;

import java.time.Duration;

public interface TokenService {

    public String createNewAccessToken(String refreshToken);
}

