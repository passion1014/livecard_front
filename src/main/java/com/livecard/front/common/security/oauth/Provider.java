package com.livecard.front.common.security.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Provider {
    KAKAO("kakao", "0"),
    NAVER("naver", "1"),
    GOOGLE("google", "2");

    private final String name;
    private final String code;

    public static String getCodeByName(String name) {
        for (Provider provider : Provider.values()) {
            if (provider.getName().equals(name)) {
                return provider.getCode();
            }
        }
        throw new IllegalArgumentException("Invalid provider name: " + name);
    }
}
