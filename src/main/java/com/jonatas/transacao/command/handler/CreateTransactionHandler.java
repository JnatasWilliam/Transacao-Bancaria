package com.jonatas.transacao.command.handler;

import com.jonatas.transacao.command.dto.CreateTransactionCommandDto;
import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import com.jonatas.transacao.command.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateTransactionHandler {

    private final TransacaoService transacaoService;

    public UUID handle(CreateTransactionCommandDto dto) {

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