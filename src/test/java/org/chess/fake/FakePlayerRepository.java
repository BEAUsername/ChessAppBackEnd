package org.chess.fake;

import org.chess.interfaces.PlayerRepository;
import org.chess.model.Credentials;
import org.chess.model.Player;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Fake database for unit tests: stores players in memory instead of in MySQL
public class FakePlayerRepository implements PlayerRepository {

    // Constants
    private static final LocalDate SEED_CREATION_DATE = LocalDate.of(2026, 1, 1);
    private static final Comparator<Player> MOST_WINS_FIRST =
            Comparator.comparingInt(Player::getWins).reversed().thenComparingInt(Player::getId);

    // Fields
    private final List<Player> players = new ArrayList<>();
    private final Map<Integer, String> passwordHashesById = new HashMap<>();
    private int nextId = 1;

    // Test helper: adds a player with statistics and no password, like the seed data in MySQL
    public Player addPlayer(String username, int wins, int losses, int draws) {
        Player player = new Player(nextId++, username, SEED_CREATION_DATE, wins, losses, draws);
        players.add(player);
        return player;
    }

    // Interface methods
    @Override
    public List<Player> findAll() {
        return List.copyOf(players);
    }

    @Override
    public Optional<Player> findById(int id) {
        return players.stream()
                .filter(player -> player.hasId(id))
                .findFirst();
    }

    @Override
    public Optional<Player> findByUsername(String username) {
        return players.stream()
                .filter(player -> player.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public Optional<Credentials> findCredentialsByUsername(String username) {
        return findByUsername(username)
                .map(player -> new Credentials(player.getUsername(), passwordHashesById.get(player.getId())));
    }

    @Override
    public List<Player> findByUsernameContaining(String text) {
        return players.stream()
                .filter(player -> player.usernameContains(text))
                .toList();
    }

    @Override
    public List<Player> findPage(String text, int limit, int offset) {
        return findByUsernameContaining(text).stream()
                .sorted(MOST_WINS_FIRST)
                .skip(offset)
                .limit(limit)
                .toList();
    }

    @Override
    public int countByUsernameContaining(String text) {
        return findByUsernameContaining(text).size();
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public void insert(String username, LocalDate creationDate, String passwordHash, LocalDate birthDate) {
        // Player has no birth date field, so the fake does not keep it
        Player player = new Player(nextId++, username, creationDate, 0, 0, 0);
        players.add(player);
        passwordHashesById.put(player.getId(), passwordHash);
    }
}