package org.chess.controller;

import org.chess.controller.dtoResponse.PlayerResponse;
import org.chess.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.chess.controller.dtoResponse.PlayerPageResponse;

@RestController
@RequestMapping("/api/players")
class PlayerController {

    // Fields
    private final PlayerService playerService;

    // Constructor: Spring injects
    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @GetMapping
    public PlayerPageResponse getPlayers(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        return playerService.getPlayerPage(username, page);
    }

    @GetMapping("/{id}")
    public PlayerResponse getPlayerById(@PathVariable("id") int id) {
        return playerService.getPlayerById(id);
    }
}