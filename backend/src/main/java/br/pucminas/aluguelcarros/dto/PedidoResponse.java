package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.PedidoAluguel;
import br.pucminas.aluguelcarros.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidoResponse(Long id, Long clienteId, String clienteNome, AutomovelResponse automovel,
                             LocalDate dataInicio, LocalDate dataFim, long dias, BigDecimal valorEstimado,
                             StatusPedido status, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {

    public static PedidoResponse from(PedidoAluguel p) {
        return new PedidoResponse(p.getId(), p.getCliente().getId(), p.getCliente().getNome(),
                AutomovelResponse.from(p.getAutomovel()), p.getDataInicio(), p.getDataFim(),
                PedidoAluguel.dias(p.getDataInicio(), p.getDataFim()), p.getValorEstimado(),
                p.getStatus(), p.getCriadoEm(), p.getAtualizadoEm());
    }
}
