FROM openjdk:17-jdk-slim
WORKDIR /app
COPY GatewayService/target/GatewayService-1.0.jar /app/GatewayService.jar
EXPOSE 9100
ENTRYPOINT ["java", "-jar", "/app/GatewayService.jar"]
