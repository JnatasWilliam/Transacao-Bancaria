package com.jonatas.transacao.query.handler;

import com.jonatas.transacao.command.model.Conta;
import com.jonatas.transacao.command.model.TipoTransacao;
import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.ContaCommandRepository;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import com.jonatas.transacao.query.dto.HistoricoTransacaoDto;
import com.jonatas.transacao.query.dto.SaldoDetalhadoResponseDto;
import com.jonatas.transacao.query.dto.TransactionResponseDto;
import com.jonatas.transacao.query.model.TransacaoLeitura;
import com.jonatas.transacao.query.repository.TransacaoLeituraRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionQueryHandler {

    private final TransacaoRepository repository;
    private final ContaCommandRepository contaCommandRepository;
    private final TransacaoLeituraRepository transacaoLeituraRepository;

    public TransactionQueryHandler(TransacaoRepository repository, ContaCommandRepository contaCommandRepository, TransacaoLeituraRepository transacaoLeituraRepository) {
        this.repository = repository;
        this.contaCommandRepository = contaCommandRepository;
        this.transacaoLeituraRepository = transacaoLeituraRepository;
    }

    public BigDecimal consultarSaldo(String login) {
        Conta conta = contaCommandRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        return conta.getSaldo();
    }

    public SaldoDetalhadoResponseDto consultarSaldoDetalhado(String login) {
        Conta conta = contaCommandRepository.findByUsuarioLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        List<Transacao> transacoes = repository.findByOrigem(login).stream()
                .filter(tx -> tx.getTipo() == TipoTransacao.SAQUE || tx.getTipo() == TipoTransacao.DEPOSITO)
                .toList();

        List<HistoricoTransacaoDto> historico = transacoes.stream()
                .map(tx -> new HistoricoTransacaoDto(
                        tx.getTipo(),
                        tx.getValor(),
                        tx.getCriadaEm().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                ))
                .toList();

        return new SaldoDetalhadoResponseDto(conta.getSaldo(), historico);
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

    public List<HistoricoTransacaoDto> consultarHistorico(String login) {
        List<TransacaoLeitura> transacoes = transacaoLeituraRepository.findByOrigem(login);

        return transacoes.stream()
                .map(tx -> new HistoricoTransacaoDto(
                        tx.getTipo(),
                        tx.getValor(),
                        tx.getCriadaEm().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                ))
                .toList();
    }

}