package com.jonatas.transacao.command.service;

import com.jonatas.transacao.command.model.Conta;
import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.ContaRepository;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    @Transactional
    public UUID realizarTransferencia(String origem, String destino, BigDecimal valor) {
        Conta contaOrigem = contaRepository.findByUsuarioLogin(origem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada"));

        Conta contaDestino = contaRepository.findByUsuarioLogin(destino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada"));

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo");
        }

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transacao transacao = Transacao.builder()
                .origem(origem)
                .destino(destino)
                .valor(valor)
                .build();

        transacaoRepository.save(transacao);

        return transacao.getId();
    }
}