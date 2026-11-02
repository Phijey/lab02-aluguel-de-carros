package br.pucminas.aluguelcarros.security;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public final class UsuarioLogado {

    private UsuarioLogado() {}

    public static boolean isAgente(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return roles != null && roles.contains("AGENTE");
    }
}
