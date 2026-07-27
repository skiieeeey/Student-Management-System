# Stage 1: Build the application using JDK 24
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Explicitly install JDK 24 binaries for the build stage environment
RUN apt-get update && apt-get install -y wget curl && \
    curl -s https://java.net | tar -xz -C /opt && \
    mv /opt/jdk-24 /opt/openjdk-24

# Set environment paths to favor our new JDK 24 setup
ENV JAVA_HOME=/opt/openjdk-24
ENV PATH="$JAVA_HOME/bin:$PATH"

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run using an official lightweight runtime structure
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Ensure runtime has the matching Java 24 context libraries
COPY --from=build /opt/openjdk-24 /opt/openjdk-24
ENV JAVA_HOME=/opt/openjdk-24
ENV PATH="$JAVA_HOME/bin:$PATH"

COPY --from=build /app/target/*.jar app.jar
EXPOSE 6969
ENTRYPOINT ["java", "-jar", "app.jar"]
