package com.jonatas.transacao.command.handler;

import com.jonatas.transacao.command.dto.CreateTransactionCommandDto;
import com.jonatas.transacao.command.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateTransactionHandlerTest {

    @Test
    void shouldPersistAndReturnGeneratedId() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("A", null)
        );

        TransacaoService transacaoService = mock(TransacaoService.class);
        CreateTransactionHandler handler = new CreateTransactionHandler(transacaoService);

        CreateTransactionCommandDto dto =
                new CreateTransactionCommandDto("B", BigDecimal.TEN);

        UUID expectedId = UUID.randomUUID();
        when(transacaoService.realizarTransferencia(any(), any(), any()))
                .thenReturn(expectedId);

        UUID result = handler.transferir(dto);

        verify(transacaoService).realizarTransferencia(any(), any(), any());
        assertNotNull(result);
    }
}