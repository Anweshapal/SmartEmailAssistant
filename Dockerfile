# Stage 1: Build Spring Boot App using Maven
FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

# Copy Maven configuration & source code
COPY pom.xml .
COPY src ./src

# Run Maven build
RUN apt-get update && apt-get install -y maven
RUN mvn -B -DskipTests clean package

# Stage 2: Create lightweight runtime image
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
