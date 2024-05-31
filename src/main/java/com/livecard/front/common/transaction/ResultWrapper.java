package com.livecard.front.common.transaction;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResultWrapper<T> {
    private ResultCode code;
    private T data;

    public static <T> ResultWrapper<T> success(T data) {

        return ResultWrapper.<T>builder()
                .code(ResultCode.R1000)
                .data(data)
                .build();
    }

    public static <T> ResultWrapper<T> adjustment(ResultCode code, T data) {
        return ResultWrapper.<T>builder()
                .code(code)
                .data(data)
                .build();
    }

    public static <T> ResultWrapper<T> failure(T data) {
        return ResultWrapper.<T>builder()
                .code(ResultCode.R9000)
                .data(data)
                .build();
    }

    public static <T> ResultWrapper<T> failure() {
        return ResultWrapper.<T>builder()
                .code(ResultCode.R9000)
                .build();
    }
}