package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AvaliacaoRequest(@NotNull Boolean aprovado,
                               @NotBlank(message = "informe o parecer") @Size(max = 500) String parecer) {
}
