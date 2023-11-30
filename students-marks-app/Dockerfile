# Dockerfile
FROM eclipse-temurin:17

RUN mkdir -p /usr/src/myapp
COPY target/students-marks-app-1.0-SNAPSHOT.jar /usr/src/myapp

RUN mkdir -p /usr/src/myapp/target
COPY target/keystore.jks /usr/src/myapp/target
COPY target/server.cer /usr/src/myapp/target

WORKDIR /usr/src/myapp
ENTRYPOINT ["java", "-jar","students-marks-app-1.0-SNAPSHOT.jar"]