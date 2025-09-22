package com.jonatas.transacao.query.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        String origem,
        String destino,
        BigDecimal valor,
        LocalDateTime criadaEm
) {}