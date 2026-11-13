package org.chess.controller;

import org.chess.controller.dtoResponse.PlayerPageResponse;
import org.chess.controller.dtoResponse.PlayerResponse;
import org.chess.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
class PlayerController {

    // Fields
    private final PlayerService playerService;

    // Constructor: Spring injects a PlayerService here
    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // GET /api/players  or  GET /api/players?username=queen&page=0
    @GetMapping
    public PlayerPageResponse getPlayers(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        return playerService.getPlayerPage(username, page);
    }

    // GET /api/players/{id}
    @GetMapping("/{id}")
    public PlayerResponse getPlayerById(@PathVariable("id") int id) {
        return playerService.getPlayerById(id);
    }

    // GET /api/players/by-username/{username}  (used by the profile page)
    @GetMapping("/by-username/{username}")
    public PlayerResponse getPlayerByUsername(@PathVariable("username") String username) {
        return playerService.getPlayerByUsername(username);
    }
}