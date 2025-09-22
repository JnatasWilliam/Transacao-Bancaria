package com.jonatas.transacao.command.controller;

import com.jonatas.transacao.command.dto.CreateTransactionCommandDto;
import com.jonatas.transacao.command.handler.CreateTransactionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/command/transacoes")
public class TransactionCommandController {

    private final CreateTransactionHandler handler;

    public TransactionCommandController(CreateTransactionHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody CreateTransactionCommandDto dto) {
        UUID id = handler.handle(dto);
        return ResponseEntity
                .created(URI.create("/api/query/transacoes/" + id))
                .build();
    }
}