package com.jaenyeong.dev.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @EnableJpaAuditing를 사용하기 위해선 최소 하나의 @Entity 클래스가 필요함
 * 그러나 @WebMbcTest 어노테이션으로 테스트할 경우 @Entity가 없음
 * 근데 @SpringBootApplication와 같이 있으면 @WebMvcTest가 @EnableJpaAuditing을 스캔하게 되어 둘을 분리
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
