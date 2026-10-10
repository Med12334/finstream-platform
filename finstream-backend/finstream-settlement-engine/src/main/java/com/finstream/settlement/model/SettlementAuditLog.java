package com.finstream.settlement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SETTLEMENT_AUDIT_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementAuditLog {

    @Id
    @Column(name = "AUDIT_ID", length = 36, nullable = false)
    private String auditId;

    @Column(name = "ORDER_ID", length = 36, nullable = false)
    private String orderId;

    @Column(name = "ACCOUNT_ID", length = 32, nullable = false)
    private String accountId;

    @Column(name = "SETTLED_AMOUNT", precision = 18, scale = 2, nullable = false)
    private BigDecimal settledAmount;

    @Column(name = "EXECUTION_FEE", precision = 10, scale = 2, nullable = false)
    private BigDecimal executionFee;

    @Column(name = "SETTLEMENT_STATUS", length = 24, nullable = false)
    private String settlementStatus;

    @Column(name = "KAFKA_OFFSET", nullable = false)
    private Long kafkaOffset;

    @Column(name = "KAFKA_PARTITION", nullable = false)
    private Integer kafkaPartition;

    @Column(name = "SETTLED_AT", nullable = false)
    private LocalDateTime settledAt;
}
