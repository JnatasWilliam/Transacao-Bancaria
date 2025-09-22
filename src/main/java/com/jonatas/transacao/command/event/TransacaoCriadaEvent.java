package com.jonatas.transacao.command.event;

import com.jonatas.transacao.command.model.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoCriadaEvent(
        UUID id,
        String origem,
        String destino,
        BigDecimal valor,
        TipoTransacao tipo,
        LocalDateTime criadaEm
) {}