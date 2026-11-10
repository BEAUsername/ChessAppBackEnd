package org.chess.repository;

import org.chess.model.Player;
import org.chess.service.PlayerRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
class DatabasePlayerRepository implements PlayerRepository {

    private static final String SELECT_PLAYERS =
            "SELECT id, username, creation_date, wins, losses, draws FROM players";

    // Fields
    private final JdbcClient jdbcClient;

    // Constructor: Spring injects a JdbcClient here
    DatabasePlayerRepository(JdbcClient jdbcClient) {
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
    public List<Player> findByUsernameContaining(String text) {
        return jdbcClient.sql(SELECT_PLAYERS + " WHERE username LIKE :text")
                .param("text", "%" + text + "%")
                .query(this::toPlayer)
                .list();
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