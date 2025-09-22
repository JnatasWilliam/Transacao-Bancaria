package com.jonatas.transacao.query.controller;

import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import com.jonatas.transacao.query.dto.TransactionResponseDto;
import com.jonatas.transacao.query.handler.TransactionQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/query/transacoes")
public class TransactionQueryController {

    private final TransactionQueryHandler handler;
    private final TransacaoRepository transacaoRepository;

    public TransactionQueryController(TransactionQueryHandler handler, TransacaoRepository transacaoRepository) {
        this.handler = handler;
        this.transacaoRepository = transacaoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> buscarPorId(@PathVariable UUID id) {
        TransactionResponseDto dto = handler.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listarTodas() {
        return ResponseEntity.ok(transacaoRepository.findAll());
    }

}