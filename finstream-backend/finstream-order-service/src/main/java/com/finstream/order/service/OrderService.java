package com.finstream.order.service;

import com.finstream.common.event.OrderEvent;
import com.finstream.order.model.OrderEntity;
import com.finstream.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderKafkaProducer kafkaProducer;

    public OrderService(OrderRepository orderRepository, OrderKafkaProducer kafkaProducer) {
        this.orderRepository = orderRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public OrderEntity placeOrder(OrderEntity order) {
        if (order.getOrderId() == null) {
            order.setOrderId(UUID.randomUUID().toString());
        }
        order.setStatus("PENDING_EXECUTION");
        OrderEntity savedOrder = orderRepository.save(order);

        OrderEvent event = new OrderEvent(
            savedOrder.getOrderId(),
            savedOrder.getAccountId(),
            savedOrder.getSymbol(),
            savedOrder.getQuantity(),
            savedOrder.getPrice(),
            savedOrder.getStatus(),
            Instant.now()
        );

        kafkaProducer.publishOrder(event);
        return savedOrder;
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }
}
