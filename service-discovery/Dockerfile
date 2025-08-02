FROM openjdk:17
WORKDIR /app
COPY target/service-discovery-0.0.1-SNAPSHOT.jar /app/service-discovery-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/service-discovery-0.0.1-SNAPSHOT.jar"]
EXPOSE 9001