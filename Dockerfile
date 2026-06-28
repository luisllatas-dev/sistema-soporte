## Etapa 1: Compilación
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Copiar archivos del proyecto Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .
COPY src ./src

# Compilar y empaquetar (sin ejecutar tests para acelerar el build)
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

## Etapa 2: Imagen final liviana
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copiar solo el JAR generado desde la etapa de build
COPY --from=build /app/target/solicitudes-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]