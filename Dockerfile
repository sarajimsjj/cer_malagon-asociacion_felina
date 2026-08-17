# Etapa 1: compila el jar con Maven (imagen solo usada durante el build, no se despliega)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagen final, solo con el JRE (más pequeña y rápida de desplegar)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Render (y la mayoría de plataformas) inyectan el puerto real en $PORT;
# server.port en application.properties ya lo respeta.
ENTRYPOINT ["java", "-jar", "app.jar"]
