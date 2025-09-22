package com.jonatas.transacao.command.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class JwtTokenProviderTest {

    private JwtTokenProvider provider;
    private final String secret = "minha-chave-secreta-32-bytes!!";
    private final long validityMs = 1_000 * 60 * 60; // 1 hora

    @BeforeEach
    void setup() {
        provider = new JwtTokenProvider();

        SecretKey key = Jwts.SIG.HS256.key().build();
        String base64 = Encoders.BASE64.encode(key.getEncoded());
        ReflectionTestUtils.setField(provider, "secretKeyBase64", base64);
        ReflectionTestUtils.setField(provider, "validityInMs", validityMs);

        provider.init(); // gera SecretKey interno
    }

    @Test
    @SuppressWarnings("unchecked")
    void generateToken_shouldContainSubjectAndRolesAndExpiration() {
        // dado um Authentication com username e role
        var auth = new UsernamePasswordAuthenticationToken(
                "user123",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // quando gerar token
        String token = provider.generateToken(auth);

        // então
        assertThat(token).isNotBlank();

        // parse das claims
        var claims = provider.getClaimsFromToken(token);
        assertThat(claims.getSubject()).isEqualTo("user123");
        assertThat(claims.get("roles", List.class))
                .containsExactly("ROLE_USER");

        // issuedAt e expiration
        Date now = new Date();
        assertThat(claims.getIssuedAt()).isBetween(
                new Date(now.getTime() - 5_000),
                new Date(now.getTime() + 5_000)
        );
        assertThat(claims.getExpiration().getTime())
                .isCloseTo(now.getTime() + validityMs, within(50L));
    }

    @Test
    void getUsernameFromToken_shouldReturnSubject() {
        var auth = new UsernamePasswordAuthenticationToken(
                "alice",
                null,
                Collections.emptyList()
        );
        String token = provider.generateToken(auth);

        String username = provider.getUsernameFromToken(token);
        assertThat(username).isEqualTo("alice");
    }

    @Test
    void validateToken_validAndExpired() throws InterruptedException {
        // token válido deve retornar true
        var auth = new UsernamePasswordAuthenticationToken(
                "bob", null, Collections.emptyList()
        );
        String token = provider.generateToken(auth);
        assertThat(provider.validateToken(token)).isTrue();

        // simulando token expirado:
        // criamos outro provider com validade mínima
        JwtTokenProvider fastExpiry = new JwtTokenProvider();
        String base64 = Base64.getEncoder().encodeToString(secret.getBytes());
        ReflectionTestUtils.setField(fastExpiry, "secretKeyBase64", base64);
        ReflectionTestUtils.setField(fastExpiry, "validityInMs", 10L);
        fastExpiry.init();

        String shortToken = fastExpiry.generateToken(auth);
        // espera o token expirar
        Thread.sleep(20L);
        assertThat(fastExpiry.validateToken(shortToken)).isFalse();
    }
}