package com.jonatas.transacao.command.dto;

// DTOs
public record RegistroRequest(
        String nomeCompleto,
        String documento,
        String login,
        String senha
) {
}
