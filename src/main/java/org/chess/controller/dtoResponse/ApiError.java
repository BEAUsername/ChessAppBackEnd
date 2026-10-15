package org.chess.controller.dtoResponse;

// Record: the JSON body sent back when something goes wrong
public record ApiError(int status, String message) {
}