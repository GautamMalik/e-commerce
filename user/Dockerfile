FROM openjdk:17
WORKDIR /app
COPY target/user-0.0.1-SNAPSHOT.jar /app/user-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/user-0.0.1-SNAPSHOT.jar"]
EXPOSE 9002