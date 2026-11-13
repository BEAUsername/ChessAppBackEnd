package org.chess.repository;

import org.chess.model.Credentials;
import org.chess.model.Player;
import org.chess.interfaces.PlayerRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
class JdbcPlayerRepository implements PlayerRepository {

    private static final String SELECT_PLAYERS =
            "SELECT id, username, creation_date, wins, losses, draws FROM players";

    // Fields
    private final JdbcClient jdbcClient;

    // Constructor: Spring injects a JdbcClient here
    JdbcPlayerRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // Interface methods
    @Override
    public List<Player> findAll() {
        return jdbcClient.sql(SELECT_PLAYERS)
                .query(this::toPlayer)
                .list();
    }

    @Override
    public Optional<Player> findById(int id) {
        return jdbcClient.sql(SELECT_PLAYERS + " WHERE id = :id")
                .param("id", id)
                .query(this::toPlayer)
                .optional();
    }

    @Override
    public Optional<Player> findByUsername(String username) {
        return jdbcClient.sql(SELECT_PLAYERS + " WHERE username = :username")
                .param("username", username)
                .query(this::toPlayer)
                .optional();
    }

    @Override
    public Optional<Credentials> findCredentialsByUsername(String username) {
        return jdbcClient.sql("SELECT username, password_hash FROM players WHERE username = :username")
                .param("username", username)
                .query((resultSet, rowNumber) -> new Credentials(
                        resultSet.getString("username"),
                        resultSet.getString("password_hash")))
                .optional();
    }

    @Override
    public List<Player> findByUsernameContaining(String text) {
        return jdbcClient.sql(SELECT_PLAYERS + " WHERE username LIKE :text")
                .param("text", "%" + text + "%")
                .query(this::toPlayer)
                .list();
    }

    @Override
    public List<Player> findPage(String text, int limit, int offset) {
        return jdbcClient.sql(SELECT_PLAYERS
                        + " WHERE username LIKE :text ORDER BY wins DESC, id ASC LIMIT :limit OFFSET :offset")
                .param("text", "%" + text + "%")
                .param("limit", limit)
                .param("offset", offset)
                .query(this::toPlayer)
                .list();
    }

    @Override
    public int countByUsernameContaining(String text) {
        return jdbcClient.sql("SELECT COUNT(*) FROM players WHERE username LIKE :text")
                .param("text", "%" + text + "%")
                .query(Integer.class)
                .single();
    }

    @Override
    public boolean existsByUsername(String username) {
        int count = jdbcClient.sql("SELECT COUNT(*) FROM players WHERE username = :username")
                .param("username", username)
                .query(Integer.class)
                .single();
        return count > 0;
    }

    @Override
    public void insert(String username, LocalDate creationDate, String passwordHash, LocalDate birthDate) {
        jdbcClient.sql("INSERT INTO players (username, creation_date, password_hash, birth_date) "
                        + "VALUES (:username, :creationDate, :passwordHash, :birthDate)")
                .param("username", username)
                .param("creationDate", creationDate)
                .param("passwordHash", passwordHash)
                .param("birthDate", birthDate)
                .update();
    }

    // Private helper: turns one database row into a Player
    private Player toPlayer(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Player(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getDate("creation_date").toLocalDate(),
                resultSet.getInt("wins"),
                resultSet.getInt("losses"),
                resultSet.getInt("draws")
        );
    }
}