# ChessBackEnd

Chess API built with Spring Boot.

## Run the app (Docker only)

```
docker run -p 8080:8080 ghcr.io/beausername/chessapp-backend:latest
```

Then open http://localhost:8080/api/players

## Develop

Requirements: Java 25 and Docker.

1. Build the jar:
```
   ./gradlew bootJar
```
2. Build the Docker image:
```
   docker build -t chessapp-backend-image .
```
3. Run it:
```
   docker run -p 8080:8080 chessapp-backend-image
```

r