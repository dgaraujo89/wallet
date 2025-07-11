package com.wallet.config;

import com.wallet.idempotency.IdempotencyAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.NoSuchAlgorithmException;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

    @Bean
    public IdempotencyAspect idempotencyAspect(RedisTemplate<String, Object> redisTemplate) throws NoSuchAlgorithmException {
        return new IdempotencyAspect(redisTemplate);
    }

}
