package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Rendimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RendimentoDTO(
        @NotBlank(message = "informe a entidade empregadora") String entidadeEmpregadora,
        @NotNull @PositiveOrZero BigDecimal valor) {

    public static RendimentoDTO from(Rendimento r) {
        return new RendimentoDTO(r.getEntidadeEmpregadora(), r.getValor());
    }
}
