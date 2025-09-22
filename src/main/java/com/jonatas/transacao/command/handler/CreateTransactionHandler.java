package com.jonatas.transacao.command.handler;

import com.jonatas.transacao.command.dto.CreateTransactionCommandDto;
import com.jonatas.transacao.command.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateTransactionHandler {

    private final TransacaoService transacaoService;

    public UUID transferir(CreateTransactionCommandDto dto) {

        String origem = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return transacaoService.realizarTransferencia(
                origem,
                dto.destino(),
                dto.valor()
        );
    }
}