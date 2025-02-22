package com.test.service_sender.service;

import com.test.service_sender.dto.ProductDTO;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Сервис для отправки JSON-сообщений в RabbitMQ.
 * Класс отвечает за отправку объектов ProductDTO в очередь.
 */
@Setter
@Service
public class JsonSender {
    private static final Logger logger = LoggerFactory.getLogger(JsonSender.class);

    @Value("${exchange.name}")
    private String exchangeName;

    @Value("${routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public JsonSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJson(ProductDTO productDTO) {
        try {
            productDTO.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

            logger.info("Получено сообщение для отправки: {}", productDTO);
            rabbitTemplate.convertAndSend(exchangeName, routingKey, productDTO);
        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщения", e);
            throw new RuntimeException("Ошибка при отправке сообщения", e);
        }
    }
}
