package com.livecard.front.common.service;

import com.livecard.front.domain.entity.OauthToken;
import com.livecard.front.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public OauthToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                //TODO: exception확인
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    @Override
    public void removeRefreshTokenByMbrUserId(Long mbrUserid) {
        refreshTokenRepository.deleteByMbrUserId(mbrUserid);
    }
}
