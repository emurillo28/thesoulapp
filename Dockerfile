FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Environment variables with defaults
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080
ENV APP_API_BASE_URL=http://localhost:8080/api
ENV APP_WEBSOCKET_URL=ws://localhost:8080/ws
ENV FEATURE_TASKS_ENABLED=false
ENV FEATURE_BLOG_ENABLED=false
ENV FEATURE_INSIGHTS_ENABLED=false

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"] 