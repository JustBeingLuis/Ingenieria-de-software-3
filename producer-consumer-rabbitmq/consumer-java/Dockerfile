# Etapa de construcción con Maven
FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Imagen final ligera con solo el JAR
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/target/consumer-1.0-SNAPSHOT-jar-with-dependencies.jar consumer.jar
CMD ["java", "-jar", "consumer.jar"]
