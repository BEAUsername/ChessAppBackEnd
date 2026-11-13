package org.chess.service;

import org.chess.controller.dtoRequests.JoinRequest;
import org.chess.controller.dtoRequests.LoginRequest;
import org.chess.exception.InvalidLoginException;
import org.chess.exception.UsernameTakenException;
import org.chess.interfaces.PlayerRepository;
import org.chess.model.Credentials;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
public class AuthService {

    // Constants
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9]{3,12}$");
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 72;

    // Fields
    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Constructor: Spring injects a PlayerRepository here
    public AuthService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Public methods (used by the controller)
    public void join(JoinRequest request) {
        validateUsername(request.username());
        validatePassword(request.password());
        validateBirthDate(request.birthDate());

        if (playerRepository.existsByUsername(request.username())) {
            throw new UsernameTakenException(request.username());
        }

        String passwordHash = passwordEncoder.encode(request.password());
        playerRepository.insert(request.username(), LocalDate.now(), passwordHash, request.birthDate());
    }

    // Checks username and password; returns the username as stored in the database
    public String login(LoginRequest request) {
        if (request.username() == null || request.password() == null) {
            throw new InvalidLoginException();
        }

        Credentials credentials = playerRepository.findCredentialsByUsername(request.username())
                .orElseThrow(InvalidLoginException::new);

        if (credentials.passwordHash() == null
                || !passwordEncoder.matches(request.password(), credentials.passwordHash())) {
            throw new InvalidLoginException();
        }

        return credentials.username();
    }

    // Validation
    private void validateUsername(String username) {
        if (username == null || !USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Username must be 3-12 characters and only contain letters or digits");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be 8-72 characters");
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
    }
}