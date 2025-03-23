FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker cache for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source files and build the application
COPY src ./src
RUN mvn package -DskipTests

# Use a smaller base image for the runtime
FROM openjdk:17-slim
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]