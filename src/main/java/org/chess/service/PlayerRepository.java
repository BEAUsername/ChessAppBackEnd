package org.chess.service;

import org.chess.model.Player;

import java.util.List;
import java.util.Optional;

// Interface: what the service needs from any kind of player storage
public interface PlayerRepository {

    List<Player> findAll();

    Optional<Player> findById(int id);

    List<Player> findByUsernameContaining(String text);
}