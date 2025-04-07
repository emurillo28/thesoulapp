# Use Amazon Corretto 21 as base image
FROM amazoncorretto:21

# Set working directory
WORKDIR /app

# Copy Gradle files
COPY gradle ./gradle
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src ./src

# Build the application
RUN ./gradlew build --no-daemon

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "build/libs/thesoulapp-0.0.1-SNAPSHOT.jar"] 