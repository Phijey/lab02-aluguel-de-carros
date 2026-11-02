package br.pucminas.aluguelcarros.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ClienteRequest(
        @NotBlank String login,
        String senha,
        @NotBlank String nome,
        @NotBlank String rg,
        @NotBlank @Size(min = 11, max = 14) String cpf,
        String profissao,
        @Valid EnderecoDTO endereco,
        @Size(max = 3, message = "no máximo 3 rendimentos") List<@Valid RendimentoDTO> rendimentos
) {}
