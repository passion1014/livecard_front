package com.livecard.front.common.service;

import com.livecard.front.domain.entity.RefreshToken;
import com.livecard.front.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                //TODO: exception확인
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
