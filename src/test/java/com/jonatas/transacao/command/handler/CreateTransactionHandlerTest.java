package com.jonatas.transacao.command.handler;

import com.jonatas.transacao.command.dto.CreateTransactionCommandDto;
import com.jonatas.transacao.command.model.Transacao;
import com.jonatas.transacao.command.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateTransactionHandlerTest {

    @Test
    void shouldPersistAndReturnGeneratedId() {
        TransacaoRepository repo = mock(TransacaoRepository.class);
        CreateTransactionHandler handler = new CreateTransactionHandler(repo);

        CreateTransactionCommandDto dto =
                new CreateTransactionCommandDto("A", "B", BigDecimal.TEN);

        when(repo.save(any(Transacao.class)))
                .thenAnswer(invocation -> {
                    Transacao tx = invocation.getArgument(0);
                    UUID generated = UUID.randomUUID();
                    ReflectionTestUtils.setField(tx, "id", generated);
                    return tx;
                });

        UUID result = handler.handle(dto);

        verify(repo).save(any(Transacao.class));
        assertNotNull(result);
    }
}