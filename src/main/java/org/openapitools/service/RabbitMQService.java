package org.openapitools.service;

import org.openapitools.configuration.RabbitMQConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


@Service
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToOcrDocumentInQueue(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ECHO_IN_QUEUE_NAME, message);
    }
}
