package org.chess.repository;

import org.chess.model.Player;
import org.chess.service.PlayerRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
class DatabasePlayerRepository implements PlayerRepository {

    // Fields: the list of available players
    private final List<Player> players = List.of(
            new Player(1, "Beau", LocalDate.of(2026, 1, 15), 12, 3, 2),
            new Player(2, "Naruto", LocalDate.of(2026, 3, 2), 8, 5, 1),
            new Player(3, "Sasuke", LocalDate.of(2026, 5, 20), 0, 0, 0),
            new Player(4, "Pikachu", LocalDate.of(2026, 5, 8), 4, 6, 3)
    );

    // Interface methods
    @Override
    public List<Player> findAll() {
        return players;
    }

    @Override
    public Optional<Player> findById(int id) {
        for (Player player : players) {
            if (player.hasId(id)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Player> findByUsernameContaining(String text) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.usernameContains(text)) {
                result.add(player);
            }
        }
        return List.copyOf(result);
    }
}