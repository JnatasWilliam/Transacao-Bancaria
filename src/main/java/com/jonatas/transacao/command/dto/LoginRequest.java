package com.jonatas.transacao.command.dto;

public record LoginRequest(
        String login,
        String senha
) {
}
