package com.jonatas.transacao.command.service;

import com.jonatas.transacao.command.event.TransacaoCriadaEvent;
import com.jonatas.transacao.command.model.Conta;
import com.jonatas.transacao.command.model.TipoTransacao;
import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.ContaCommandRepository;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final ContaCommandRepository contaCommandRepository;
    private final TransacaoRepository transacaoRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public UUID realizarTransferencia(String origem, String destino, BigDecimal valor) {
        Conta contaOrigem = contaCommandRepository.findByUsuarioLogin(origem)
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada"));

        Conta contaDestino = contaCommandRepository.findByUsuarioLogin(destino)
                .orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada"));

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo");
        }

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaCommandRepository.save(contaOrigem);
        contaCommandRepository.save(contaDestino);

        Transacao transacao = Transacao.builder()
                .origem(origem)
                .destino(destino)
                .valor(valor)
                .tipo(TipoTransacao.TRANSFERENCIA)
                .build();

        transacaoRepository.save(transacao);

        applicationEventPublisher.publishEvent(new TransacaoCriadaEvent(
                transacao.getId(),
                transacao.getOrigem(),
                transacao.getDestino(),
                transacao.getValor(),
                transacao.getTipo(),
                transacao.getCriadaEm()
        ));

        return transacao.getId();
    }

    @Transactional
    public UUID depositar(String login, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo");
        }

        Conta conta = contaCommandRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        conta.setSaldo(conta.getSaldo().add(valor));
        contaCommandRepository.save(conta);

        Transacao transacao = Transacao.builder()
                .origem(login)
                .destino(login)
                .valor(valor)
                .tipo(TipoTransacao.DEPOSITO)
                .build();

        transacaoRepository.save(transacao);

        applicationEventPublisher.publishEvent(new TransacaoCriadaEvent(
                transacao.getId(),
                transacao.getOrigem(),
                transacao.getDestino(),
                transacao.getValor(),
                transacao.getTipo(),
                transacao.getCriadaEm()
        ));

        return transacao.getId();
    }

    @Transactional
    public UUID sacar(String login, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo");
        }

        Conta conta = contaCommandRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para saque");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaCommandRepository.save(conta);

        Transacao transacao = Transacao.builder()
                .origem(login)
                .destino(null)
                .valor(valor)
                .tipo(TipoTransacao.SAQUE)
                .build();

        transacaoRepository.save(transacao);

        applicationEventPublisher.publishEvent(new TransacaoCriadaEvent(
                transacao.getId(),
                transacao.getOrigem(),
                transacao.getDestino(),
                transacao.getValor(),
                transacao.getTipo(),
                transacao.getCriadaEm()
        ));

        return transacao.getId();
    }
}