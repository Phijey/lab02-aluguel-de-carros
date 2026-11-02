package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PedidoAgenteRequest(@NotNull LocalDate dataInicio, @NotNull LocalDate dataFim,
                                  @NotNull @PositiveOrZero BigDecimal valorEstimado) {}
