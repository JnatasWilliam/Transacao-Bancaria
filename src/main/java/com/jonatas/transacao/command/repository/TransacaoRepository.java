package com.jonatas.transacao.command.repository;

import com.jonatas.transacao.command.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
}