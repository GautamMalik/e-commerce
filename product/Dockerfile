FROM openjdk:17
WORKDIR /app
COPY target/product-0.0.1-SNAPSHOT.jar /app/product-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/product-0.0.1-SNAPSHOT.jar"]
EXPOSE 9003