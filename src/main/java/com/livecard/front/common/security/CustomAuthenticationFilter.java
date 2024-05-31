package com.livecard.front.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livecard.front.common.exception.BrowserSessionExpiredEvent;
import com.livecard.front.common.transaction.ResultWrapper;
import com.livecard.front.dto.member.LoginDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private ApplicationContext applicationContext;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDto loginDto;
        try {
            loginDto = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e); // 적절한 예외 처리 필요
        }


        String combinedUsername = loginDto.getUsername();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                combinedUsername,
                loginDto.getPassword()
        );
        authRequest.setDetails(loginDto); // loginDto 정보를 details에 저장

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);

        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();

        //같은 사용자의 이전 세션들을 찾습니다.
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails.getUsername(), false);
//        System.out.println("All existing sessions for the user: " + sessions.toString());

        for (SessionInformation session : sessions) {
            // 각 세션의 정보 출력
//            System.out.println("Session ID: " + session.getSessionId());
//            System.out.println("Session Principal (Expected same as UserDetails): " + session.getPrincipal());
            if (!session.getSessionId().equals(request.getSession().getId())) {
                session.expireNow(); // 다른 세션 만료
                applicationContext.publishEvent(new BrowserSessionExpiredEvent(this, session.getSessionId(),"DUPLICATE_LOGIN"));
//                System.out.println("Session expired: " + session.getSessionId());
            }
        }


        // 새 세션 생성
        HttpSession session = request.getSession(true);
        SecurityContextHolder.getContext().setAuthentication(authResult);

        // Register new session
        String sessionId = request.getSession().getId();
        sessionRegistry.registerNewSession(sessionId, userDetails.getUsername());

        // 세션 시간 설정
        LoginDto loginDto = (LoginDto) authResult.getDetails();
        int extendedSessionTimeInSeconds = loginDto.isRemember() ? (60 * 60) * 24 : (60 * 60); // 체크시 24시간 : 미체크시 1시간
//        session.setAttribute("UserInfo",authResult.getPrincipal());
        session.setMaxInactiveInterval(extendedSessionTimeInSeconds);
        session.setAttribute("loginTime", System.currentTimeMillis());
        session.setAttribute("shortGnb", false);

//        // 세션 내 모든 속성 이름을 가져오기
//        Enumeration<String> attributeNames = session.getAttributeNames();
//
//        System.out.println(session.getId());
//        System.out.println(attributeNames);
//
//// 속성 이름을 반복하고 그 값을 출력하기
//        while (attributeNames.hasMoreElements()) {
//            String attributeName = attributeNames.nextElement();
//            Object attributeValue = session.getAttribute(attributeName);
//            System.out.println("속성 이름: " + attributeName);
//            System.out.println("속성 값: " + attributeValue);
//        }

        HashMap<String, String> resltData = new HashMap<>() ;
        resltData.put("msg","로그인 성공") ;
        resltData.put("sessionId",sessionId) ;


        // JSON 응답 및 UTF-8 인코딩 설정
        response.setContentType("application/json;charset=UTF-8");
        ResultWrapper<HashMap<String, String>> result = ResultWrapper.success(resltData);
        response.setStatus(HttpStatus.OK.value());
        new ObjectMapper().writeValue(response.getWriter(), result);
    }




    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        System.out.println("??? 여기로오지??");
        System.out.println(failed instanceof DisabledException);


        HashMap<String, String> resltData = new HashMap<>() ;

        // 실패 응답 작성
        ResultWrapper<String> result;
        if (failed instanceof UsernameNotFoundException) {
//            System.out.println("응답1");
            resltData.put("msg","일치하는 아이디가 존재하지 않습니다.") ;
        } else if (failed instanceof BadCredentialsException) {
//            System.out.println("응답2");
            resltData.put("msg","비밀번호가 일치하지 않습니다.") ;
        } else if (failed instanceof DisabledException || failed.getCause() instanceof DisabledException) {
//            System.out.println("응답3");
            resltData.put("msg","관리자의 상태가 활성화 되어 있지 않습니다.") ;
        } else {
//            System.out.println("응답4");
            resltData.put("msg","로그인에 실패했습니다.") ;
        }

        response.setContentType("application/json;charset=UTF-8");
        ResultWrapper<HashMap<String, String>> failRes = ResultWrapper.failure(resltData);
        response.setStatus(HttpStatus.OK.value());
        new ObjectMapper().writeValue(response.getWriter(), failRes);
    }
}