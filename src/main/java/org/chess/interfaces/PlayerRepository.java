package org.chess.interfaces;

import org.chess.model.Credentials;
import org.chess.model.Player;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Interface: what the services need from any kind of player storage
public interface PlayerRepository {

    List<Player> findAll();

    Optional<Player> findById(int id);

    Optional<Player> findByUsername(String username);

    Optional<Credentials> findCredentialsByUsername(String username);

    List<Player> findByUsernameContaining(String text);

    List<Player> findPage(String text, int limit, int offset);

    int countByUsernameContaining(String text);

    boolean existsByUsername(String username);

    void insert(String username, LocalDate creationDate, String passwordHash, LocalDate birthDate);
}