package org.chess.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenServiceTest {

    private static final String TEST_SECRET = "test-secret-only-for-unit-tests-0123456789";
    private static final String OTHER_SECRET = "another-secret-that-is-also-long-enough-123";

    private TokenService tokenService;
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        SecretKey key = createKey(TEST_SECRET);
        tokenService = new TokenService(new NimbusJwtEncoder(new ImmutableSecret<>(key)));
        jwtDecoder = createDecoder(key);
    }

    @Test
    void createToken_withUsername_putsUsernameInSubject() {
        // Act
        Jwt jwt = jwtDecoder.decode(tokenService.createToken("Naruto"));

        // Assert
        assertEquals("Naruto", jwt.getSubject());
    }

    @Test
    void createToken_withUsername_expiresAfterOneHour() {
        // Act
        Jwt jwt = jwtDecoder.decode(tokenService.createToken("Naruto"));

        // Assert
        Duration lifetime = Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt());
        assertEquals(Duration.ofHours(1), lifetime);
    }

    @Test
    void createToken_checkedWithOtherSecret_isRejected() {
        // Arrange
        JwtDecoder otherDecoder = createDecoder(createKey(OTHER_SECRET));
        String token = tokenService.createToken("Naruto");

        // Act and Assert
        assertThrows(JwtException.class, () -> otherDecoder.decode(token));
    }

    // Private helpers
    private static SecretKey createKey(String secret) {
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    private static JwtDecoder createDecoder(SecretKey key) {
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }
}