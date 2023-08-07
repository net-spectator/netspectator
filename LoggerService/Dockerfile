FROM openjdk:17-jdk-slim
WORKDIR /app
COPY LoggerService/target/LoggerService-1.0-exec.jar /app/LoggerService.jar
EXPOSE 9070
ENTRYPOINT ["java", "-jar", "/app/LoggerService.jar"]
