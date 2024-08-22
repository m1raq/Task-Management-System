FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/taskManagementSystem-0.0.1-SNAPSHOT.jar
COPY build/libs/taskManagementSystem-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]