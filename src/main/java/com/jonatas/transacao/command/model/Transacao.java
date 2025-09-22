package com.jonatas.transacao.command.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacoes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // construtor vazio para JPA
public class Transacao {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 36)
    private String origem;

    @Column(nullable = false, length = 36)
    private String destino;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDateTime criadaEm;

    @Builder  // gera um builder com origem, destino e valor
    public Transacao(String origem,
                     String destino,
                     BigDecimal valor) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
        this.criadaEm = LocalDateTime.now();
    }
}