package com.jonatas.transacao.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroRequestDto(
        @NotBlank
        @Size(max = 100)
        String nomeCompleto,

        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "Documento deve conter 11 dígitos numéricos")
        String documento,

        @NotBlank
        @Size(min = 4, max = 20)
        String login,

        @NotBlank
        @Size(min = 8)
        String senha
) {
}
