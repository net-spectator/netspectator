FROM openjdk:17-jdk-slim
WORKDIR /app
COPY UserManagementService/target/UserManagementService-1.0-exec.jar /app/UserManagementService.jar
EXPOSE 9080
ENTRYPOINT ["java", "-jar", "/app/UserManagementService.jar"]
