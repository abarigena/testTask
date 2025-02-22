FROM eclipse-temurin:21-jre-alpine

COPY target/*.jar /app/app.jar

COPY wait-for-rabbitmq.sh /wait-for-rabbitmq.sh

RUN chmod +x /wait-for-rabbitmq.sh

EXPOSE 8000

ENTRYPOINT ["/wait-for-rabbitmq.sh"]
