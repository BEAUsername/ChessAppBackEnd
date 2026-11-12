# ChessBackEnd

Chess API built with Spring Boot (Java 25), using a MySQL database.

## Run the whole app

The back end, front end and database are started together from a different repository with Docker Compose.
The Docker image of the BE can be found here: 

ghcr.io/beausername/chessapp-backend:latest

## Develop

Requirements: Java 25 and a MySQL database.

1. Set these environment variables (for example in the IntelliJ run configuration):
    - `SPRING_DATASOURCE_URL`, for example `jdbc:mysql://localhost:3306/chessapp`
    - `SPRING_DATASOURCE_USERNAME`
    - `SPRING_DATASOURCE_PASSWORD`
2. Start `ChessBackEndApplication`. Flyway creates the tables on the first start.
3. Open http://localhost:8080/api/players