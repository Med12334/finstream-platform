package com.finstream.order.service;

import com.finstream.common.event.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderKafkaProducer.class);
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${app.kafka.topics.order-inbound}")
    private String inboundTopic;

    public OrderKafkaProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrder(OrderEvent event) {
        log.info("Dispatching OrderEvent to Kafka [topic={}, orderId={}]", inboundTopic, event.orderId());
        kafkaTemplate.send(inboundTopic, event.orderId(), event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Kafka dispatch confirmed [offset={}, partition={}]", 
                        result.getRecordMetadata().offset(), 
                        result.getRecordMetadata().partition());
                } else {
                    log.error("Kafka dispatch failed for orderId: {}", event.orderId(), ex);
                }
            });
    }
}
