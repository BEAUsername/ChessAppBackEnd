package org.chess.controller.dtoResponse;

import java.util.List;

public record PlayerPageResponse(
        List<PlayerResponse> players,
        int page,
        int totalPages
) {
}