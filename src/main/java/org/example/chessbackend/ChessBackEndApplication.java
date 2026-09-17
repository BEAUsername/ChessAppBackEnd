package org.example.chessbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.example.chessbackend", "org.chess"})
public class ChessBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChessBackEndApplication.class, args);
    }

}
