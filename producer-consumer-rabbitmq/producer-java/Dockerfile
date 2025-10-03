# Etapa 1: Compilación con Maven
FROM maven:3.9.6-eclipse-temurin-11 AS builder

WORKDIR /app

# Copiamos pom.xml y resolvemos dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Compilamos y empaquetamos (genera JAR con dependencias en target/)
RUN mvn package -DskipTests

# Etapa 2: Imagen final liviana
FROM eclipse-temurin:11-jre

WORKDIR /app

# Copiamos el JAR desde la etapa builder
COPY --from=builder /app/target/producer-java-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Comando por defecto
CMD ["java", "-jar", "app.jar"]
