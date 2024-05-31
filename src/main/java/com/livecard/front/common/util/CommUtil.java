package com.livecard.front.common.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class CommUtil {
    /**
     * 문자열을 Character로 변환
     */
    public static Character toChar(String str) {
        Character result = null;
        if (str != null && !str.isEmpty()) {
            result = str.charAt(0);
        }
        return result;
    }

    /**
     * Character를 문자열로 변환
     */
    public static String toStr(Character ch) {
        String result = null;
        if (ch != null) {
            result = ch.toString();
        }
        return result;
    }

    public static LocalDate toLocalDate(String format, String val) {
        LocalDate result = null;
        if (val != null && !val.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            result = LocalDate.parse(val, formatter);
        }
        return result;
    }

    public static LocalDate toLocalDate(String val) {
        return toLocalDate("yyyyMMdd", val);
    }

    public static LocalDate toLocalDateFormat(String format, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(dateString, formatter);
    }

    public static LocalDateTime toLocalDateTime(String format, String val) {
        LocalDateTime result = null;
        if (val != null && !val.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            result = LocalDateTime.parse(val, formatter);
        }
        return result;
    }

    public static LocalDateTime toLocalDateTime(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                // ISO 8601 형식을 처리하는 경우
                if (value.contains("T")) {
                    return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
                }
                // 기존의 'yyyyMMddHHmmss' 형식을 처리하는 경우
                else if (value.length() == 8) {
                    value += "000000";
                    return toLocalDateTime("yyyyMMddHHmmss", value);
                }
            } catch (DateTimeParseException e) {
                // 오류 처리
                // 적절한 로그 기록이나 예외 처리를 할 수 있습니다.
            }
        }
        return null;
    }



    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }



    /**
     * 요청 타입과 에러 코드에 따른 에러 처리, 기본 에러 메시지 포함
     */
    public static void handleRequestTypeError(HttpServletRequest request, HttpServletResponse response, int errorCode, String errorMessage) throws IOException {
        // 기본 에러 메시지 설정
        String defaultErrorMessage = getDefaultErrorMessage(errorCode);
        String finalErrorMessage = errorMessage != null ? errorMessage : defaultErrorMessage;

        // 요청 URI 얻기
        String uri = request.getRequestURI();

        // URI가 '/api'를 포함하는지 확인
        boolean isApiRequest = uri.startsWith("/api") || uri.contains("/api/");

        if(isApiRequest){
            // REST API 요청의 경우 JSON 형태의 에러 응답 반환
            response.setContentType("application/json");
            response.setStatus(errorCode);
            response.getWriter().write("{\"error\": \"" + finalErrorMessage + "\"}");
        }else{
            // 일반 웹 페이지 요청의 경우 에러 페이지로 리다이렉트
            response.sendRedirect("/error/" + errorCode);
        }
    }

    /**
     * 에러 코드에 따른 기본 에러 메시지 반환
     */
    private static String getDefaultErrorMessage(int errorCode) {
        switch (errorCode) {
            case 401:
                return "Unauthorized";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 500:
                return "Internal Server Error";
            default:
                return "Error";
        }
    }

    public static boolean isMobileDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            return userAgent.matches(".*(mobile|android|webos|iphone|ipod|blackberry|iemobile|opera mini).*");
        }
        return false;
    }

    public static boolean isTabletDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            return userAgent.matches(".*(ipad|android).*") && !userAgent.matches(".*mobile.*");
        }
        return false;
    }

    public static String determineViewPath(HttpServletRequest request, String basePath, String mobilePath) {
        boolean isMobile = isMobileDevice(request);
        boolean isTablet = isTabletDevice(request);
        boolean isPcViewMode = Boolean.TRUE.equals(request.getSession().getAttribute("isPcViewMode"));

        // 모바일일 경우
        if (isMobile) {
            // 세션이 PC 뷰 모드를 요구하는지 확인
            if (isPcViewMode) {
                return basePath;
            } else {
                // mobilePath가 제공되면 해당 경로를 사용, 그렇지 않으면 기본 경로에 "/mobile" 추가
                return (mobilePath != null) ? mobilePath : "mobile/" + basePath;
            }
            // 모바일이 아닐 경우 (태블릿 포함)
        } else {
            if (isTablet) {
                System.out.println("테블릿 접속");
            }
            return basePath;
        }
    }



}
