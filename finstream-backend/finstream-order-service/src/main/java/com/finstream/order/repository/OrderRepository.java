package com.finstream.order.repository;

import com.finstream.order.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByAccountIdOrderByCreatedAtDesc(String accountId);
    List<OrderEntity> findByStatusOrderByCreatedAtDesc(String status);
}
