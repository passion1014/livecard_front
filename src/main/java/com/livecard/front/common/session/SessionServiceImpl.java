package com.livecard.front.common.session;

import com.livecard.front.common.exception.BrowserSessionExpiredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRegistry sessionRegistry;
    private final ApplicationContext applicationContext;

    /**
     * 세션 키를 사용하여 세션을 찾는 메서드.
     *
     * @param sessionKey 세션 키 (예: memberFarmCode_memberId)
     * @return 찾아진 HttpSession 객체. 존재하지 않으면 null을 반환.
     */
    public List<SessionInformation> findSessionBySessionKey(String sessionKey) {
        return sessionRegistry.getAllSessions(sessionKey, false);
    }

    @Override
    public void publishSessionExpiryEvent(String sessionKey, String logoutType) {
        List<SessionInformation> sessions = findSessionBySessionKey(sessionKey);
        for (SessionInformation session : sessions) {
            if (!session.isExpired()) { // 이미 만료되지 않은 세션에 대해
                session.expireNow(); // 세션 만료
                applicationContext.publishEvent(new BrowserSessionExpiredEvent(this, session.getSessionId(), logoutType));
            }
        }
    }

}
