# Stage 1: Build the app using OpenJDK 23 and Maven
FROM openjdk:23-slim AS build

# Install Maven (and its dependencies)
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

WORKDIR /build

# Copy pom.xml and download dependencies first (caching dependencies)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the app using OpenJDK 23
FROM openjdk:23-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /build/target/vet-0.0.1-SNAPSHOT.jar ./vet.jar

# Expose the port if needed (for example, 8080)
EXPOSE 8080

# Set the entrypoint to run the app
ENTRYPOINT ["java", "-jar", "vet.jar"]
