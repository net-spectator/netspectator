FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY repo /app/repo
COPY LoggerService /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true spring-boot:repackage

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/LoggerService.jar
EXPOSE 9070
ENTRYPOINT ["java", "-jar", "/app/LoggerService.jar"]
