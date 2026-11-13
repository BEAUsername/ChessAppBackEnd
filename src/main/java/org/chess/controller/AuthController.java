package org.chess.controller;

import org.chess.controller.dtoRequests.JoinRequest;
import org.chess.controller.dtoRequests.LoginRequest;
import org.chess.controller.dtoResponse.AccountResponse;
import org.chess.controller.dtoResponse.LoginResponse;
import org.chess.service.AuthService;
import org.chess.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    // Fields
    private final AuthService authService;
    private final TokenService tokenService;

    // Constructor: Spring injects both services here
    AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    // Endpoints

    // POST /api/auth/join -> 201 Created
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public void join(@RequestBody JoinRequest request) {
        authService.join(request);
    }

    // POST /api/auth/login -> 200 OK with { token, username }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String username = authService.login(request);
        String token = tokenService.createToken(username);
        return new LoginResponse(token, username);
    }

    // GET /api/auth/me -> who does the token in the Authorization header belong to?
    @GetMapping("/me")
    public AccountResponse me(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return new AccountResponse(null);
        }
        return new AccountResponse(jwt.getSubject());
    }
}