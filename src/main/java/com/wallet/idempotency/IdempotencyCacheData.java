package com.wallet.idempotency;

public record IdempotencyCacheData(boolean status,
                                   String hash,
                                   Object data,
                                   Exception exception) {

}
