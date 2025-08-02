FROM openjdk:17
WORKDIR /app
COPY target/notification-0.0.1-SNAPSHOT.jar /app/notification-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/notification-0.0.1-SNAPSHOT.jar"]
EXPOSE 9006