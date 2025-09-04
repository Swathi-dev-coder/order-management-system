package com.oms.order_service.mqconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrderNotification(NotificationMessage  message) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, message);
    }
}
