package org.chess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {

    @GetMapping("/api/chess/status")
    public String status() {
        return "Chess backend is running";
    }
}
