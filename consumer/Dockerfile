# Dockerfile
FROM eclipse-temurin:17

RUN mkdir -p /usr/src/myapp
COPY target/consumer-1.0.jar /usr/src/myapp
WORKDIR /usr/src/myapp
ENTRYPOINT ["java", "-jar","consumer-1.0.jar"]
