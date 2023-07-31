FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY UserService/target/UserService-1.0-exec.jar /app/UserService.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/UserService.jar"]
