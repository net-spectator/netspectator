FROM openjdk:17-jdk-slim
WORKDIR /app
COPY inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar /app/inventoryservice.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app/inventoryservice.jar"]
