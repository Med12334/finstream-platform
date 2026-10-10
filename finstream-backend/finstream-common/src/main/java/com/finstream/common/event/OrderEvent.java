package com.finstream.common.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record OrderEvent(
    String orderId,
    String accountId,
    String symbol,
    BigDecimal quantity,
    BigDecimal price,
    String status,
    Instant timestamp
) implements Serializable {}
