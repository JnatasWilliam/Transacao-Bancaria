package com.jonatas.transacao.command.dto;

public record RegistroRequestDto(
        String nomeCompleto,
        String documento,
        String login,
        String senha
) {
}
