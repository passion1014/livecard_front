package com.livecard.front.common.session;

import org.springframework.security.core.session.SessionInformation;

import java.util.List;

public interface SessionService {
    List<SessionInformation> findSessionBySessionKey(String sessionKey);
    void publishSessionExpiryEvent(String sessionKey, String logoutType);
}
