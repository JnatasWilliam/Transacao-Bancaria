package com.jonatas.transacao.command.handler;

import com.jonatas.transacao.command.dto.CreateTransactionCommandDto;
import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CreateTransactionHandler {

    private final TransacaoRepository repository;

    public CreateTransactionHandler(TransacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UUID handle(CreateTransactionCommandDto dto) {
        Transacao tx = Transacao.builder()
                .origem(dto.origem())
                .destino(dto.destino())
                .valor(dto.valor())
                .build();
        return repository.save(tx)
                .getId();
    }
}