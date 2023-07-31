FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY inventory-service/target/inventory-service-0.0.1-SNAPSHOT.jar /app/inventoryservice.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app/inventoryservice.jar"]
