package com.jonatas.transacao.command.dto;

import java.math.BigDecimal;

public record CreateTransactionCommandDto(String destino, BigDecimal valor) {}


