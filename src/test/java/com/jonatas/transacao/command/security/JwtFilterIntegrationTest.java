package com.jonatas.transacao.command.security;

import com.jonatas.transacao.TransacaoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = TransacaoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtFilterIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Test
    void shouldReturn401WithoutToken() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/protected", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldAccessProtectedEndpointWithValidToken() {
        var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "jonatas", null, java.util.List.of()
        );
        String token = tokenProvider.generateToken(auth);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("/api/protected", HttpMethod.GET, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Bem-vindo");
    }
}
