package com.livecard.front.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshExpiredException extends RuntimeException {

    public TokenRefreshExpiredException(String token) {
        super("refresh token expired " + token);
    }
}
