package com.test.service_sender.Controller;

import com.test.service_sender.dto.ProductDTO;
import com.test.service_sender.service.JsonSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для отправки сообщений в RabbitMQ.
 * Этот класс обрабатывает HTTP-запросы и отправляет данные в очередь.
 */
@RestController
public class SenderController {
    private static final Logger logger = LoggerFactory.getLogger(SenderController.class);

    private final JsonSender jsonSender;

    @Autowired
    public SenderController(JsonSender jsonSender) {
        this.jsonSender = jsonSender;
    }

    /**
     * Обрабатывает POST-запрос для отправки продукта в RabbitMQ.
     *
     * @param productDTO Объект продукта для отправки.
     * @return Ответ с результатом операции.
     */
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody ProductDTO productDTO) {
        logger.info("Получен запрос на отправку товара: {}", productDTO);
        if (productDTO == null) {
            return ResponseEntity.badRequest().body("Некоректные данные");
        }
        try {
            jsonSender.sendJson(productDTO);
            logger.info("Сообщение отправлено в RabbitMQ");
            return ResponseEntity.ok("Сообщение отправлено в RabbitMQ");
        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщения", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при отправке сообщения");
        }
    }
}
