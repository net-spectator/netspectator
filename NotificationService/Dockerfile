FROM openjdk:17-jdk-slim
WORKDIR /app
COPY NotificationService/target/NotificationService-0.0.1-SNAPSHOT.jar /app/NotificationService.jar
EXPOSE 8190
ENTRYPOINT ["java", "-jar", "/app/NotificationService.jar"]
