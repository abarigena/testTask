package com.test.service_sender.config;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки RabbitMQ.
 * Класс создаёт и настраивает бины, необходимые для работы с RabbitMQ.
 */
@Setter
@Configuration
public class RabbitConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);

    @Value("${queue.name}")
    private String queueName;

    @Value("${exchange.name}")
    private String exchangeName;

    @Value("${routing.key}")
    private String routingKey;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    /**
     * Создаёт очередь RabbitMQ.
     *
     * @return Очередь с именем, указанным в конфигурации.
     */
    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    /**
     * Создаёт обменник типа Direct.
     *
     * @return Обменник с именем, указанным в конфигурации.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    /**
     * Создаёт привязку между очередью и обменником.
     *
     * @return Привязка с ключом маршрутизации, указанным в конфигурации.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
    }

    /**
     * Создаёт соединений RabbitMQ.
     *
     * @return соединений с настройками пользователя и пароля.
     */
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("rabbitmq");
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);

        return connectionFactory;
    }

    /**
     * Создаёт RabbitAdmin для управления RabbitMQ.
     *
     * @return RabbitAdmin, настроенный с соединений.
     */
    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /**
     * Конвертер преобразует сообщения в формате JSON в объекты Java и наоборот.
     *
     * @return Конвертер сообщений.
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Создаёт и настраивает RabbitTemplate для отправки сообщений в RabbitMQ.
     * Настраивает обработку подтверждений и ошибок.
     *
     * @return Настроенный RabbitTemplate.
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());

        // Обработка подтверждений доставки
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if(ack){
                logger.info("Сообщение успешно доставлено в очередь");
            } else{
                logger.info("Доставка сообщения в очередь не удалась: {}", cause);
            }
        });

        // Обработка недоставленных сообщений
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            logger.error("Сообщение не доставлено. Причина: {}, Сообщение: {}",
                    returnedMessage.getReplyText(),
                    returnedMessage.getMessage());
        });

        rabbitTemplate.setMandatory(true);

        return rabbitTemplate;
    }
}
