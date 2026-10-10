package com.finstream.settlement.repository;

import com.finstream.settlement.model.SettlementAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementAuditRepository extends JpaRepository<SettlementAuditLog, String> {
    List<SettlementAuditLog> findByAccountId(String accountId);
    List<SettlementAuditLog> findByOrderId(String orderId);
}
