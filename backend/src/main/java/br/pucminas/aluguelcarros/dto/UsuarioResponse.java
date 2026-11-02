package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Perfil;
import br.pucminas.aluguelcarros.model.Usuario;

public record UsuarioResponse(Long id, String login, String nome, Perfil perfil) {

    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getLogin(), u.getNome(), u.getPerfil());
    }
}
