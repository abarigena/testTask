package com.test.service_sender;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceSenderApplication {

	private final RabbitAdmin rabbitAdmin;

	private final Queue queue;

	@Autowired
	public ServiceSenderApplication(RabbitAdmin rabbitAdmin, Queue queue) {
		this.rabbitAdmin = rabbitAdmin;
		this.queue = queue;
	}

	/**
	 * Метод, который выполняется после запуска приложения.
	 * Объявляет очередь в RabbitMQ.
	 */
	@PostConstruct
	public void declareQueue() {
		rabbitAdmin.declareQueue(queue);
	}

	public static void main(String[] args) {
		SpringApplication.run(ServiceSenderApplication.class, args);
	}

}
