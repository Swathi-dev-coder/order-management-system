package com.oms.notification_service.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {
	@RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Notification received: " + message);
        // You can add more complex notification logic here (email, SMS, etc.)
    }
}
