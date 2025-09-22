package com.jonatas.transacao.query.dto;

import java.math.BigDecimal;
import java.util.List;

public record SaldoDetalhadoResponseDto(
        BigDecimal saldoTotal,
        List<HistoricoTransacaoDto> historico
) {
}
