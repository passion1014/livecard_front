package com.livecard.front.common.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * 캐시 설정을 위한 구성 클래스.
 * 이 클래스는 Spring Boot 애플리케이션에서 캐시 기능을 활성화한다.
 * @EnableCaching 어노테이션은 스프링의 캐시 추상화를 활성화한다.
 */
@Configuration
@EnableCaching
public class CacheConfig {
    // 기본적으로 Spring Boot의 내장 캐시 매니저를 사용합니다.
    // 추가적인 캐시 설정이 필요한 경우 여기에 정의합니다.
}
