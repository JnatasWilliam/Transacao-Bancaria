package com.jonatas.transacao.command.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.jonatas.transacao.command.model.Conta;
import com.jonatas.transacao.command.repository.ContaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonatas.transacao.command.model.Usuario;
import com.jonatas.transacao.command.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra um novo usuário, valida duplicidade e persiste no banco.
     *
     * @param nomeCompleto nome completo do usuário
     * @param documento    CPF/CNPJ (11 dígitos)
     * @param login        apelido de acesso (username)
     * @param senhaPlain   senha em texto plano
     * @return UUID do usuário criado
     * @throws IllegalArgumentException se documento ou login já existirem
     */
    @Transactional
    public UUID registrar(String nomeCompleto,
                          String documento,
                          String login,
                          String senhaPlain) {

        if (usuarioRepository.existsByDocumento(documento)) {
            throw new IllegalArgumentException("Documento já cadastrado: " + documento);
        }

        if (usuarioRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Login já existe: " + login);
        }

        String hash = passwordEncoder.encode(senhaPlain);
        Usuario usuario = Usuario.builder()
                .nomeCompleto(nomeCompleto)
                .documento(documento)
                .login(login)
                .senhaHash(hash)
                .build();

        usuarioRepository.save(usuario);

        Conta conta = Conta.builder()
                .usuario(usuario)
                .saldo(BigDecimal.ZERO)
                .build();

        contaRepository.save(conta);

        return usuario.getId();
    }

    /**
     * Carrega usuário pelo login para autenticação Spring Security.
     *
     * @param username nome de usuário enviado no login
     * @return UserDetails com dados de autenticação
     * @throws UsernameNotFoundException se não encontrar o usuário
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}