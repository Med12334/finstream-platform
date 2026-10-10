package com.finstream.settlement.consumer;

import com.finstream.common.event.OrderEvent;
import com.finstream.settlement.model.SettlementAuditLog;
import com.finstream.settlement.repository.SettlementAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderSettlementConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderSettlementConsumer.class);
    private final SettlementAuditRepository auditRepository;

    public OrderSettlementConsumer(SettlementAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Transactional
    @KafkaListener(
        topics = "${app.kafka.topics.order-inbound}",
        groupId = "finstream-settlement-group"
    )
    public void consumeOrder(
        @Payload OrderEvent orderEvent,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("Processing order {} from Kafka partition {} offset {}", 
            orderEvent.orderId(), partition, offset);

        // Core Settlement Math: calculate total gross amount and execution fee
        BigDecimal grossAmount = orderEvent.price().multiply(orderEvent.quantity());
        BigDecimal executionFee = grossAmount.multiply(BigDecimal.valueOf(0.0015))
            .setScale(2, RoundingMode.HALF_UP);
        BigDecimal settledAmount = grossAmount.subtract(executionFee);

        // Save immutable audit record into IBM DB2
        SettlementAuditLog auditEntry = SettlementAuditLog.builder()
            .auditId(UUID.randomUUID().toString())
            .orderId(orderEvent.orderId())
            .accountId(orderEvent.accountId())
            .settledAmount(settledAmount)
            .executionFee(executionFee)
            .settlementStatus("COMPLETED_SETTLED")
            .kafkaPartition(partition)
            .kafkaOffset(offset)
            .settledAt(LocalDateTime.now())
            .build();

        auditRepository.save(auditEntry);
        log.info("Persisted settlement audit record {} to IBM DB2", auditEntry.getAuditId());
    }
}
