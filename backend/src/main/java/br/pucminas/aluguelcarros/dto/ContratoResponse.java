package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.ContratoCredito;
import br.pucminas.aluguelcarros.model.TipoProprietario;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoResponse(Long id, PedidoResponse pedido, String agente, TipoProprietario tipoProprietario,
                               LocalDate dataAssinatura, CreditoResponse credito) {

    public record CreditoResponse(String banco, BigDecimal valor, BigDecimal taxaJuros, Integer numeroParcelas,
                                  BigDecimal valorParcela) {
        static CreditoResponse from(ContratoCredito c) {
            if (c == null) return null;
            return new CreditoResponse(c.getBanco().getNome(), c.getValor(), c.getTaxaJuros(), c.getNumeroParcelas(), c.valorParcela());
        }
    }

    public static ContratoResponse from(Contrato c) {
        return new ContratoResponse(c.getId(), PedidoResponse.from(c.getPedido()), c.getAgente().getNome(),
                c.getTipoProprietario(), c.getDataAssinatura(), CreditoResponse.from(c.getContratoCredito()));
    }
}
