package com.jonatas.transacao.query.controller;

import com.jonatas.transacao.command.model.Conta;
import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.ContaRepository;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import com.jonatas.transacao.query.dto.SaldoDetalhadoResponseDto;
import com.jonatas.transacao.query.dto.SaldoResponseDto;
import com.jonatas.transacao.query.dto.TransactionResponseDto;
import com.jonatas.transacao.query.handler.TransactionQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/query/transacoes")
public class TransactionQueryController {

    private final TransactionQueryHandler handler;
    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;

    public TransactionQueryController(TransactionQueryHandler handler, TransacaoRepository transacaoRepository, ContaRepository contaRepository) {
        this.handler = handler;
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
    }

    @GetMapping("/saldo")
    public ResponseEntity<SaldoResponseDto> consultarSaldo() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        BigDecimal saldo = handler.consultarSaldo(login);
        return ResponseEntity.ok(new SaldoResponseDto(saldo));
    }

    @GetMapping("/saldodetalhado")
    public ResponseEntity<SaldoDetalhadoResponseDto> consultarSaldoDetalhado() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        SaldoDetalhadoResponseDto dto = handler.consultarSaldoDetalhado(login);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<TransactionResponseDto>> listarMinhasTransacoes() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TransactionResponseDto> transacoes = handler.findByUsuario(login);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> buscarPorId(@PathVariable UUID id) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        TransactionResponseDto dto = handler.findById(id, login);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/todas")
    public ResponseEntity<List<Transacao>> listarTodas() {
        return ResponseEntity.ok(transacaoRepository.findAll());
    }

    @GetMapping("/idconta")
    public ResponseEntity<String> buscarIdConta() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        UUID idConta = contaRepository.findByUsuarioLogin(login)
                .map(Conta::getId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        return ResponseEntity.ok(idConta.toString());
    }

}