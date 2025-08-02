FROM openjdk:17
WORKDIR /app
COPY target/payment-0.0.1-SNAPSHOT.jar /app/payment-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/payment-0.0.1-SNAPSHOT.jar"]
EXPOSE 9005