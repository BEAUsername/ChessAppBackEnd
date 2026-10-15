FROM eclipse-temurin:25-jre
WORKDIR /app
COPY build/libs/ChessBackEnd-0.0.1.jar ChessApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ChessApp.jar"]