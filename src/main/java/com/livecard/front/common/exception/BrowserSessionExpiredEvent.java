package com.livecard.front.common.exception;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BrowserSessionExpiredEvent extends ApplicationEvent {
    private final String sessionId;
    private final String logoutType; // "DUPLICATE_LOGIN" 또는 "SESSION_EXPIRED"

    public BrowserSessionExpiredEvent(Object source, String sessionId, String logoutType) {
        super(source);
        this.sessionId = sessionId;
        this.logoutType = logoutType;
    }

}