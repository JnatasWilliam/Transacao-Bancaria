package com.jonatas.transacao.query.repository;


import com.jonatas.transacao.command.model.TipoTransacao;
import com.jonatas.transacao.query.model.TransacaoLeitura;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface TransacaoLeituraRepository extends MongoRepository<TransacaoLeitura, UUID> {
    List<TransacaoLeitura> findByOrigem(String origem);
    List<TransacaoLeitura> findByOrigemAndTipoIn(String origem, List<TipoTransacao> tipos);
    List<TransacaoLeitura> findByOrigemAndTipoOrderByCriadaEmDesc(String origem, TipoTransacao tipo);
}
