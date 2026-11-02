package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Endereco;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(String logradouro, String numero, String bairro, String cidade,
                          @Size(max = 2) String uf, String cep) {

    public static EnderecoDTO from(Endereco e) {
        if (e == null) return null;
        return new EnderecoDTO(e.getLogradouro(), e.getNumero(), e.getBairro(), e.getCidade(), e.getUf(), e.getCep());
    }

    public Endereco toModel() {
        return new Endereco(logradouro, numero, bairro, cidade, uf, cep);
    }
}
