package org.chess.dto;

// Record: the JSON body sent back when something goes wrong
public record ApiError(int status, String message) {
}