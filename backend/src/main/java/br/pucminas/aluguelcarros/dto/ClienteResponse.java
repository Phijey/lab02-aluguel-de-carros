package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Cliente;

import java.util.List;

public record ClienteResponse(Long id, String login, String nome, String rg, String cpf, String profissao,
                              EnderecoDTO endereco, List<RendimentoDTO> rendimentos) {

    public static ClienteResponse from(Cliente c) {
        return new ClienteResponse(c.getId(), c.getLogin(), c.getNome(), c.getRg(), c.getCpf(), c.getProfissao(),
                EnderecoDTO.from(c.getEndereco()),
                c.getRendimentos().stream().map(RendimentoDTO::from).toList());
    }
}
