package com.jonatas.transacao.command.repository;

import com.jonatas.transacao.command.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContaRepository extends JpaRepository<Conta, UUID> {

    Optional<Conta> findByUsuarioLogin(String login);
}
