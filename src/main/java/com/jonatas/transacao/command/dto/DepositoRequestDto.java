package com.jonatas.transacao.command.dto;

import java.math.BigDecimal;

public record DepositoRequestDto(
        String login,
        BigDecimal valor
) {}
