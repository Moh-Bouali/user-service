# Dockerfile for Spring Boot microservice
FROM openjdk:21-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]