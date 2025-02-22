#!/bin/sh
until nc -z rabbitmq 5672; do
  echo "Waiting for RabbitMQ..."
  sleep 2
done
java -jar /app/app.jar
