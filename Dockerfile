FROM openjdk:19-jdk-slim
WORKDIR /app

ARG APP_NAME=lineage
ARG VERSION=1.0.11

ARG JAR_FILE=target/${APP_NAME}-${VERSION}.jar
COPY ${JAR_FILE} app.jar

RUN chmod +x app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]