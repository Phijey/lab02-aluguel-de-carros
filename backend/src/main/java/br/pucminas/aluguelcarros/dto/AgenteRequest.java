package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AgenteRequest(
        @NotBlank String login,
        @NotBlank @Size(min = 6, message = "a senha deve ter pelo menos 6 caracteres") String senha,
        @NotBlank String nome,
        @NotBlank String cnpj,
        @NotNull Perfil tipo) {
}
