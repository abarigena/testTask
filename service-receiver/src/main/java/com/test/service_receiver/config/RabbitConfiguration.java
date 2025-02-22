package com.test.service_receiver.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки RabbitMQ.
 * Класс создаёт и настраивает бины, необходимые для работы с RabbitMQ.
 */
@Configuration
public class RabbitConfiguration {

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
     * Настраиваем слушателей RabbitMQ, устанавливаем соединение, ручное подтверждение
     * сообщений, конвертер.
     *
     * @param connectionFactory соединений RabbitMQ.
     * @return Настроенная фабрика контейнеров.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}
