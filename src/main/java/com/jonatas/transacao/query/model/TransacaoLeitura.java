package com.jonatas.transacao.query.model;

import com.jonatas.transacao.command.model.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "transacoes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoLeitura {

    @Id
    private UUID id;

    private String origem;
    private String destino;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private LocalDateTime criadaEm;
}
