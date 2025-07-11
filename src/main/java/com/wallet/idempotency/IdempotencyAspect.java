package com.wallet.idempotency;

import com.wallet.exceptions.IdempotencyException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Aspect
public class IdempotencyAspect {

    private static final String IDEMPOTENCY_CACHE_PREFIX = "idempotency:";
    private static final long CACHE_EXPIRATION_SECONDS = 3600;

    private final MessageDigest digest;
    private final RedisTemplate<String, Object> redisTemplate;

    public IdempotencyAspect(RedisTemplate<String, Object> redisTemplate) throws NoSuchAlgorithmException {
        this.digest = MessageDigest.getInstance("SHA-1");
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(com.wallet.idempotency.Idempotent)")
    public void idempotencyPointcut() {
    }

    @Around("idempotencyPointcut() && @annotation(idempotent)")
    public Object handleIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        final var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            final var correlationId = attributes.getRequest().getHeader(idempotent.header());

            final var hash = getIdempotentArgsHash(joinPoint);

            final var cachedResponse = getCachedResponse(correlationId, hash);
            if (cachedResponse != null) {
                if (cachedResponse.status()) {
                    if (!hash.equals(cachedResponse.hash())) {
                        throw new IdempotencyException(
                                "A different request with the same correlation ID and arguments has already been processed. " +
                                        "Please use a unique correlation ID for each request.");
                    }

                    return cachedResponse.data();
                }

                throw cachedResponse.exception();
            }

            try {
                final var response = joinPoint.proceed();
                final var cacheData = new IdempotencyCacheData(true, hash, response, null);
                saveResponseInCache(correlationId, hash, cacheData);
                return response;
            } catch (Exception e) {
                final var cacheData = new IdempotencyCacheData(false, hash, null, e);
                saveResponseInCache(correlationId, hash, cacheData);
                throw e;
            }
        }

        return joinPoint.proceed();
    }

    private String getIdempotentArgsHash(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();
        List<String> idempotentArgs = new ArrayList<>();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(IdempotentArg.class)) {
                idempotentArgs.add(String.valueOf(args[i].hashCode()));
            }
        }

        final var body = String.join(":", idempotentArgs);
        return bytesToHex(digest.digest(body.getBytes(StandardCharsets.UTF_8)));
    }

    private IdempotencyCacheData getCachedResponse(String correlationId, String hash) {
        String key = IDEMPOTENCY_CACHE_PREFIX + correlationId;
        return (IdempotencyCacheData) redisTemplate.opsForValue().get(key);
    }

    private void saveResponseInCache(String correlationId, String hash, IdempotencyCacheData response) {
        String key = IDEMPOTENCY_CACHE_PREFIX + correlationId;
        redisTemplate.opsForValue().set(key, response, CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
