package com.jonatas.transacao.query.dto;

import com.jonatas.transacao.command.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        String origem,
        String destino,
        BigDecimal valor,
        LocalDateTime criadaEm
) {
    public static TransactionResponseDto fromModel(Transacao tx) {
        return new TransactionResponseDto(
                tx.getId(),
                tx.getOrigem(),
                tx.getDestino(),
                tx.getValor(),
                tx.getCriadaEm()
        );
    }

}