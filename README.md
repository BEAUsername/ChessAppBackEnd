# ChessBackEnd

Chess API built with Spring Boot.

## Run with Docker

Requirements: Java 25 and Docker.

1. Build the jar:
```
   ./gradlew bootJar
```
2. Build the Docker image:
```
   docker build -t chessapp-backend .
```
3. Start the container:
```
   docker run -p 8080:8080 chessapp-backend
```

The API is now available at http://localhost:8080/api/players.

## Why step 1 is needed

The Dockerfile copies the jar from `build/libs/`. That folder is not in Git, so you have to build the jar first.