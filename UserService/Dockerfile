FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY repo /app/repo
COPY UserService /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true spring-boot:repackage

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/*.jar"]
