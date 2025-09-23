package com.jonatas.transacao.query.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonatas.transacao.command.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, UUID> {
    Optional<Conta> findByUsuarioLogin(String login);
}

