package com.jonatas.transacao.query.dto;

import com.jonatas.transacao.command.model.TipoTransacao;

import java.math.BigDecimal;

public record HistoricoTransacaoDto(
        TipoTransacao tipo,
        BigDecimal valor,
        String data
) {
}
