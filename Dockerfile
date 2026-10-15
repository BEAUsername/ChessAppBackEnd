FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/build/libs/ChessBackEnd-0.0.1.jar ChessApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ChessApp.jar"]
