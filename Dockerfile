FROM openjdk:19-jdk-slim
WORKDIR /app

ARG APP_NAME=lineage
ARG VERSION=1.0.0

ARG JAR_FILE=target/${APP_NAME}-${VERSION}.jar
ADD ${JAR_FILE} ${APP_NAME}-${VERSION}.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar", "/${APP_NAME}-${VERSION}.jar"]
