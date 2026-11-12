package org.chess.controller;

import org.chess.controller.dtoRequests.JoinRequest;
import org.chess.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
class AuthController {


    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public void join(@RequestBody JoinRequest request) {
        authService.join(request);
    }
}