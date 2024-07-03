package com.livecard.front.common.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The type Default response body.
 */
@Getter
@NoArgsConstructor
public class DefaultResponseBody {
    private String code;
    private String message;
    private Object data;
    private String description;

    /**
     * Instantiates a new Default response body.
     *
     * @param code        the code
     * @param message     the message
     * @param data        the data
     * @param description the description
     */
    @Builder
    public DefaultResponseBody(String code, String message, Object data, String description) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }
}
