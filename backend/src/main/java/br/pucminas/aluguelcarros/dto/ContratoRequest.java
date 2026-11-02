package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.TipoProprietario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ContratoRequest(@NotNull Long pedidoId,
                              @NotNull TipoProprietario tipoProprietario,
                              @Valid Credito credito) {

    public record Credito(Long bancoId,
                          @NotNull @Positive BigDecimal valor,
                          @NotNull @PositiveOrZero BigDecimal taxaJuros,
                          @NotNull @Min(1) Integer numeroParcelas) {}
}
