package com.test.service_receiver.service;

import com.rabbitmq.client.Channel;
import com.test.service_receiver.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Слушатель сообщений из RabbitMQ.
 * Получает сообщения из очереди и обрабатывает их.
 */
@Service
public class RabbitReceiver {
    private static final Logger logger = LoggerFactory.getLogger(RabbitReceiver.class);

    private final ProductService productService;

    @Autowired
    public RabbitReceiver(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Обрабатывает сообщения из очереди RabbitMQ.
     * Сообщения преобразуются в объект ProductDTO и сохраняются в базе данных.
     *
     * @param product Объект продукта, полученный из сообщения.
     * @param channel Канал RabbitMQ для подтверждения обработки сообщения.
     * @param tag Тег доставки сообщения.
     * @throws IOException Если произошла ошибка при работе с каналом.
     */
    @RabbitListener(queues = {"Product.queue"}, containerFactory = "rabbitListenerContainerFactory")
    public void receive(ProductDTO product, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            logger.info("Получено сообщение: id = {}, name = {}, Price = {}, Timestamp = {}",
                    product.getId(), product.getName(), product.getPrice(), product.getTimestamp());

            productService.addNewProduct(product);

            channel.basicAck(tag, false);
        } catch (Exception e) {
            logger.error("Ошибка при обработке сообщения: id = {}, name = {}, Price = {}, Timestamp = {}. Ошибка: {}",
                    product.getId(), product.getName(), product.getPrice(), product.getTimestamp(), e.getMessage(), e);

            channel.basicNack(tag, false, true);
        }
    }
}
