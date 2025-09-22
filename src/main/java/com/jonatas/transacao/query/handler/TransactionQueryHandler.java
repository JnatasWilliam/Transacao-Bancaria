package com.jonatas.transacao.query.handler;

import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import com.jonatas.transacao.query.dto.TransactionResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionQueryHandler {

    private final TransacaoRepository repository;

    public TransactionQueryHandler(TransacaoRepository repository) {
        this.repository = repository;
    }

    public TransactionResponseDto findById(UUID id) {
        Transacao tx = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        return new TransactionResponseDto(
                tx.getId(),
                tx.getOrigem(),
                tx.getDestino(),
                tx.getValor(),
                tx.getCriadaEm()
        );
    }
}