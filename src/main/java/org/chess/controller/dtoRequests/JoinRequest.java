package org.chess.controller.dtoRequests;

import java.time.LocalDate;

public record JoinRequest(String username, String password, LocalDate birthDate) {
}