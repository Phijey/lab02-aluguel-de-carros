package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Perfil;

public record TokenResponse(String token, Long id, String nome, Perfil perfil) {}
