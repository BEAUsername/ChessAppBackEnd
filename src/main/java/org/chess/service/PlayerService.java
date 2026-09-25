package org.chess.service;

import org.chess.dto.PlayerResponse;
import org.chess.exception.PlayerNotFoundException;
import org.chess.model.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    // Fields
    private final PlayerRepository playerRepository;

    // Constructor: Spring injects a PlayerRepository here
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Public methods (used by the controller)
    public List<PlayerResponse> getPlayers(String usernameSearch) {
        List<Player> players = findPlayers(usernameSearch);
        return toResponses(players);
    }

    public PlayerResponse getPlayerById(int id) {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isEmpty()) {
            throw new PlayerNotFoundException(id);
        }
        return PlayerResponse.fromPlayer(player.get());
    }

    // Private helpers
    private List<Player> findPlayers(String usernameSearch) {
        if (usernameSearch == null) {
            return playerRepository.findAll();
        }
        if (usernameSearch.isBlank()) {
            return playerRepository.findAll();
        }
        return playerRepository.findByUsernameContaining(usernameSearch);
    }

    private List<PlayerResponse> toResponses(List<Player> players) {
        List<PlayerResponse> responses = new ArrayList<>();
        for (Player player : players) {
            responses.add(PlayerResponse.fromPlayer(player));
        }
        return List.copyOf(responses);
    }
}