FROM openjdk:17-jdk-slim
WORKDIR /app
RUN mkdir resources
COPY WebDeviceService/target/WebDeviceService-0.0.1-SNAPSHOT.jar /app/WebDeviceService.jar
COPY WebDeviceService/src/main/resources/server.properties /app/resources
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/app/WebDeviceService.jar"]
