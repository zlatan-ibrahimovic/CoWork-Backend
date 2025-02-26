# Étape 1 : Build l'application
# FROM docker-dev-virtual.repository.pole-emploi.intra/maven/docker-pe-maven-3.8.6-jdk-17-owasp:1.0.0 AS build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Exécuter l'application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
