# Dockerfile for Spring Boot microservice
FROM openjdk:21-jdk

# Copy CA cert into the image
COPY ca.crt /usr/local/share/ca-certificates/ca.crt

# Import the CA certificate into the default Java truststore
RUN keytool -import -noprompt -trustcacerts \
  -alias myCA \
  -file /usr/local/share/ca-certificates/ca.crt \
  -keystore $JAVA_HOME/lib/security/cacerts \
  -storepass changeit

VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]