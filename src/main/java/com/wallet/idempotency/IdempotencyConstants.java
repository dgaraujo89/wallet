package com.wallet.idempotency;

public interface IdempotencyConstants {

    String IDEMPOTENCY_CACHE_PREFIX = "idempotency:";
    long CACHE_EXPIRATION_SECONDS = 3600;
    String DEFAULT_HEADER = "X-Correlation-ID";
    String IDEMPOTENCY_HASH_ALGORITHM = "SHA-1";

}
