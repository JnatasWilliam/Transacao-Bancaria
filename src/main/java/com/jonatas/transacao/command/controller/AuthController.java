package com.jonatas.transacao.command.controller;

import com.jonatas.transacao.command.dto.AuthResponseDto;
import com.jonatas.transacao.command.dto.LoginRequestDto;
import com.jonatas.transacao.command.dto.RegistroRequestDto;
import com.jonatas.transacao.command.security.JwtTokenProvider;
import com.jonatas.transacao.command.util.CpfValidator;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.jonatas.transacao.command.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/command/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistroRequestDto request) {

        if (!CpfValidator.isValid(request.documento())) {
            return ResponseEntity.badRequest().body("CPF inválido");
        }
        usuarioService.registrar(
                request.nomeCompleto(),
                request.documento(),
                request.login(),
                request.senha()
        );

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.senha())
        );

        String token = jwtTokenProvider.generateToken(auth);
        return ResponseEntity.ok(new AuthResponseDto(token));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.login(), req.senha())
        );

        String token = jwtTokenProvider.generateToken(auth);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}