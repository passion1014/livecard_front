package com.livecard.front.common.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultCode {
    R1000("Success"),
    R2000("No results"),
    R3000("Not authenticated"),
    R4000("Expired"),
    R5000("Invalid parameter"),
    R6000("Violate Data"),
    R9000("Failure");

    private final String message;
}
