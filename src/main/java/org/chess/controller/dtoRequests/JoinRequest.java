package org.chess.controller.dtoRequests;

import java.time.LocalDate;

// Record: the JSON body that arrives when a visitor creates an account
public record JoinRequest(String username, String password, LocalDate birthDate) {
}