package org.chess.controller;

import org.chess.dto.PlayerResponse;
import org.chess.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/players")
class PlayerController {

    // Fields
    private final PlayerService playerService;

    // Constructor: Spring injects a PlayerService here
    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // Endpoints

    // GET /api/players  or  GET /api/players?username=queen
    @GetMapping
    public List<PlayerResponse> getPlayers(@RequestParam(name = "username", required = false) String username) {
        return playerService.getPlayers(username);
    }

    // GET /api/players/{id}
    @GetMapping("/{id}")
    public PlayerResponse getPlayerById(@PathVariable("id") int id) {
        return playerService.getPlayerById(id);
    }
}