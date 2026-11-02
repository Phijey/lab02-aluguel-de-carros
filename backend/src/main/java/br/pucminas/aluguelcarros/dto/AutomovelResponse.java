package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.TipoProprietario;

import java.math.BigDecimal;

public record AutomovelResponse(Long id, String matricula, Integer ano, String marca, String modelo, String placa,
                                BigDecimal valorDiaria, TipoProprietario tipoProprietario, String proprietario) {

    public static AutomovelResponse from(Automovel a) {
        return new AutomovelResponse(a.getId(), a.getMatricula(), a.getAno(), a.getMarca(), a.getModelo(), a.getPlaca(),
                a.getValorDiaria(), a.getTipoProprietario(),
                a.getProprietario() == null ? null : a.getProprietario().getNome());
    }
}
