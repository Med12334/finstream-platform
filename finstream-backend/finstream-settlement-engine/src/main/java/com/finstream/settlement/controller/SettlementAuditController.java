package com.finstream.settlement.controller;

import com.finstream.settlement.model.SettlementAuditLog;
import com.finstream.settlement.repository.SettlementAuditRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settlements")
@CrossOrigin(origins = "*")
public class SettlementAuditController {

    private final SettlementAuditRepository repository;

    public SettlementAuditController(SettlementAuditRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<SettlementAuditLog>> getAllSettlements() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<SettlementAuditLog>> getByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(repository.findByOrderId(orderId));
    }
}
