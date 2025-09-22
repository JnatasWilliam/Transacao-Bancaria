package com.jonatas.transacao.query.controller;

import com.jonatas.transacao.query.dto.TransactionResponseDto;
import com.jonatas.transacao.query.handler.TransactionQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/query/transacoes")
public class TransactionQueryController {

    private final TransactionQueryHandler handler;

    public TransactionQueryController(TransactionQueryHandler handler) {
        this.handler = handler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> buscarPorId(@PathVariable UUID id) {
        TransactionResponseDto dto = handler.findById(id);
        return ResponseEntity.ok(dto);
    }
}