package com.livecard.front.common.web;

import com.livecard.front.common.exception.BrowserSessionExpiredEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SessionExpiredListener implements HttpSessionListener {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Long loginTime = (Long) se.getSession().getAttribute("loginTime");
        if (loginTime != null) {
            long logoutTime = System.currentTimeMillis();
            long duration = logoutTime - loginTime;

            // 로그인 시간과 로그아웃 시간을 콘솔에 출력
            System.out.println("Login Time: " + new Date(loginTime));
            System.out.println("Logout Time: " + new Date(logoutTime));
            System.out.println("Session Duration: " + duration + " ms");
        }

        // 세션 만료 시 BrowserSessionExpiredEvent 이벤트 발행
        String sessionId = se.getSession().getId();
        applicationContext.publishEvent(new BrowserSessionExpiredEvent(this, sessionId,"SESSION_EXPIRED"));
    }
}