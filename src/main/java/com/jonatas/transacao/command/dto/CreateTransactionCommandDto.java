package com.jonatas.transacao.command.dto;

import java.math.BigDecimal;

public record CreateTransactionCommandDto(String origem, String destino, BigDecimal valor) {}


