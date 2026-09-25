# ---------- Stage 1: build the application ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew bootJar --no-daemon

# ---------- Stage 2: run the application ----------
FROM eclipse-temurin:25-jre
WORKDIR /app
RUN groupadd --system chess && useradd --system --gid chess chess
COPY --from=build /app/build/libs/*.jar app.jar
USER chess
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]