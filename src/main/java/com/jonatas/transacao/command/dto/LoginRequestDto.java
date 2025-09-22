package com.jonatas.transacao.command.dto;

public record LoginRequestDto(
        String login,
        String senha
) {
}
