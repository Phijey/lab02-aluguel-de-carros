package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AutomovelRequest(
        @NotBlank String matricula,
        @NotNull @Min(1950) @Max(2100) Integer ano,
        @NotBlank String marca,
        @NotBlank String modelo,
        @NotBlank @Size(min = 7, max = 8) String placa,
        @NotNull @Positive BigDecimal valorDiaria
) {}
