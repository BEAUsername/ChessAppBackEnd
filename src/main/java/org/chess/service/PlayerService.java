package org.chess.service;

import org.chess.controller.dtoResponse.PlayerPageResponse;
import org.chess.controller.dtoResponse.PlayerResponse;
import org.chess.exception.PlayerNotFoundException;
import org.chess.interfaces.PlayerRepository;
import org.chess.model.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    // Constants
    private static final int PAGE_SIZE = 20;
    private static final int MAX_PAGES = 5;

    // Fields
    private final PlayerRepository playerRepository;

    // Constructor: Spring injects a PlayerRepository here
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Public methods
    public PlayerPageResponse getPlayerPage(String usernameSearch, int page) {
        String text = cleanSearch(usernameSearch);
        int totalMatches = playerRepository.countByUsernameContaining(text);
        int totalPages = Math.min((totalMatches + PAGE_SIZE - 1) / PAGE_SIZE, MAX_PAGES);
        int safePage = Math.max(0, Math.min(page, totalPages - 1));
        int offset = safePage * PAGE_SIZE;

        List<Player> players = playerRepository.findPage(text, PAGE_SIZE, offset);
        return new PlayerPageResponse(toResponses(players), safePage, totalPages);
    }

    public PlayerResponse getPlayerById(int id) {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isEmpty()) {
            throw new PlayerNotFoundException(id);
        }
        return PlayerResponse.fromPlayer(player.get());
    }

    // Private helpers
    private String cleanSearch(String usernameSearch) {
        if (usernameSearch == null) {
            return "";
        }
        return usernameSearch.trim();
    }

    private List<PlayerResponse> toResponses(List<Player> players) {
        List<PlayerResponse> responses = new ArrayList<>();
        for (Player player : players) {
            responses.add(PlayerResponse.fromPlayer(player));
        }
        return List.copyOf(responses);
    }
}