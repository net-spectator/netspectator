FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY repo /app/repo
COPY WebDeviceService /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true spring-boot:repackage

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN mkdir resources
COPY --from=builder /app/target/*.jar /app/*.jar
#COPY WebDeviceService/target/*.jar /app/*.jar
COPY WebDeviceService/src/main/resources/server.properties /app/resources
#COPY WebDeviceService/target/*.jar /app/*.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/app/*.jar"]
