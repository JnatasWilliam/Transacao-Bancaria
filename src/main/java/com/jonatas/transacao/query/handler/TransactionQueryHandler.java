package com.jonatas.transacao.query.handler;

import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import com.jonatas.transacao.query.dto.TransactionResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionQueryHandler {

    private final TransacaoRepository repository;

    public TransactionQueryHandler(TransacaoRepository repository) {
        this.repository = repository;
    }

    public List<TransactionResponseDto> findByUsuario(String login) {
        List<Transacao> transacoes = repository.findByOrigemOrDestino(login, login);
        return transacoes.stream()
                .map(TransactionResponseDto::fromModel)
                .toList();
    }

    public TransactionResponseDto findById(UUID id, String login) {
        Transacao tx = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        if (!tx.getOrigem().equals(login) && !tx.getDestino().equals(login)) {
            throw new SecurityException("Você não tem permissão para visualizar esta transação");
        }


        return new TransactionResponseDto(
                tx.getId(),
                tx.getOrigem(),
                tx.getDestino(),
                tx.getValor(),
                tx.getCriadaEm()
        );
    }
}