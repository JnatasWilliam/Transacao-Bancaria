package com.jonatas.transacao.command.controller;

import java.util.UUID;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.jonatas.transacao.command.service.UsuarioService;
import lombok.RequiredArgsConstructor;

// DTOs
record RegistroRequest(
        String nomeCompleto,
        String documento,
        String login,
        String senha
) {}

record LoginRequest(
        String login,
        String senha
) {}

record AuthResponse(
        String token
) {}

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<UUID> register(@Valid @RequestBody RegistroRequest req) {
        UUID usuarioId = usuarioService.registrar(
                req.nomeCompleto(),
                req.documento(),
                req.login(),
                req.senha()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioId);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.login(), req.senha())
        );

        String token = jwtTokenProvider.generateToken(auth);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}