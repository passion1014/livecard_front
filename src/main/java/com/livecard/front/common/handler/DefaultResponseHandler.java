package com.livecard.front.common.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Controller AOP Advice Class
 */
@ControllerAdvice(basePackages = {"com.livecard.front"})
public class DefaultResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * Response body에 기본 성공 코드값을 매핑
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {

        if (selectedContentType != null && selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            return DefaultResponseBody.builder()
                    .code("R1000")
                    .message("정상적으로 수행되었습니다.")
                    .data(body)
                    .build();
        }
        else {
            return body;
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

}
