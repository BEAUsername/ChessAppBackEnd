package org.chess.controller;

import org.chess.controller.dtoResponse.ApiError;
import org.chess.exception.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
class GlobalExceptionHandler {

    // Exception handlers

    // PlayerNotFoundException -> 404 Not Found
    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlePlayerNotFound(PlayerNotFoundException exception) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    // Unknown URL, for example /api/unknown -> 404 Not Found
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUnknownUrl(NoResourceFoundException exception) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), "This address does not exist");
    }

    // Wrong type in the URL, for example /api/players/abc -> 400 Bad Request
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleWrongType(MethodArgumentTypeMismatchException exception) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(),
                "'" + exception.getValue() + "' is not a valid value for " + exception.getName());
    }

    // Invalid data, for example a username that breaks the Player rules -> 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidData(IllegalArgumentException exception) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    // Anything unexpected -> 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnexpected(Exception exception) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong on the server");
    }
}