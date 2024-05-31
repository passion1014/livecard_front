package com.livecard.front.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.NoPermissionException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SearchNotFoundException.class)
    public ResponseEntity<?> handleSensorNotFoundException(SearchNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("BAD REQUEST", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<?> handleCustomRuntimeException(CustomRuntimeException e) {
        System.out.println("여기가 실행이 된다면 반드시 ResponseEntity가 리턴되어야함.");
        ErrorResponse errorResponse = new ErrorResponse("BAD REQUEST", e.getMessage());
        System.out.println(errorResponse);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<?> handleNoPermissionException(NoPermissionException e) {
        // JSON 형식의 에러 메시지 생성
        ErrorResponse errorResponse = new ErrorResponse("Access Denied", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }


    // ErrorResponse 클래스 정의
    @Getter
    static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

    }
}
