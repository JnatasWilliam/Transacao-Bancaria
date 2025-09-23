package com.jonatas.transacao.command.controller;

import com.jonatas.transacao.command.dto.CreateTransactionCommandDto;
import com.jonatas.transacao.command.dto.DepositoRequestDto;
import com.jonatas.transacao.command.dto.SaqueRequestDto;
import com.jonatas.transacao.command.handler.CreateTransactionHandler;
import com.jonatas.transacao.command.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/command/transacoes")
public class TransactionCommandController {

    private final CreateTransactionHandler handler;
    private final TransacaoService transacaoService;

    public TransactionCommandController(CreateTransactionHandler handler, TransacaoService transacaoService) {
        this.handler = handler;
        this.transacaoService = transacaoService;
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Void> transferir(@RequestBody CreateTransactionCommandDto dto) {
        UUID id = handler.transferir(dto);
        return ResponseEntity
                .created(URI.create("/api/query/transacoes/" + id))
                .build();
    }

    @PostMapping("/deposito")
    public ResponseEntity<Void> depositar(@RequestBody @Valid DepositoRequestDto dto) {
        UUID id = transacaoService.depositar(dto.login(), dto.valor());
        return ResponseEntity.created(URI.create("/api/query/transacoes/" + id)).build();
    }

    @PostMapping("/saque")
    public ResponseEntity<Void> sacar(@RequestBody @Valid SaqueRequestDto dto) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID id = transacaoService.sacar(login, dto.valor());
        return ResponseEntity.created(URI.create("/api/query/transacoes/" + id)).build();
    }

}
