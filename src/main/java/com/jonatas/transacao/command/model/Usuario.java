package com.jonatas.transacao.command.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue
    UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nome_completo", nullable = false)
    String nomeCompleto;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "Documento deve conter 11 dígitos numéricos")
    @Column(name = "documento", unique = true, nullable = false, length = 11)
    String documento;

    @NotBlank
    @Size(min = 4, max = 20)
    @Column(name = "nome_usuario", unique = true, nullable = false)
    String login;

    @NotBlank
    @Size(min = 8)
    @Column(name = "password_hash", nullable = false)
    String senhaHash;

    @Builder(builderClassName = "UserBuilder", buildMethodName = "build")
    public Usuario(
            String nomeCompleto,
            String documento,
            String login,
            String senhaHash
    ) {
        validarCPE(documento);
        this.nomeCompleto = nomeCompleto;
        this.documento   = documento;
        this.login       = login;
        this.senhaHash   = senhaHash;
    }

    private void validarCPE(String doc) {
        if (doc.chars().distinct().count() < 2) {
            throw new IllegalArgumentException("Documento inválido para CPE");
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}