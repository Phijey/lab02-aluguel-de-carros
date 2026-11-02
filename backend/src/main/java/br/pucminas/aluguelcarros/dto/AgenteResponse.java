package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Perfil;

public record AgenteResponse(Long id, String login, String nome, String cnpj, Perfil perfil) {
    public static AgenteResponse from(Agente a) {
        return new AgenteResponse(a.getId(), a.getLogin(), a.getNome(), a.getCnpj(), a.getPerfil());
    }
}
