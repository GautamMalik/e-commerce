FROM openjdk:17
WORKDIR /app
COPY target/order-0.0.1-SNAPSHOT.jar /app/order-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/order-0.0.1-SNAPSHOT.jar"]
EXPOSE 9004