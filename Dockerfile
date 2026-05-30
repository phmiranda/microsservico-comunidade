FROM openjdk:8-jdk-alpine

ARG BUILD_FILE=target/*.jar

COPY ${BUILD_FILE} app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app.jar"]
