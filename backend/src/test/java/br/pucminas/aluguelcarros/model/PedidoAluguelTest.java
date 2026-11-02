package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PedidoAluguelTest {

    Automovel carro;
    Cliente cliente;
    LocalDate hoje = LocalDate.now();

    @BeforeEach
    void setup() {
        carro = new Automovel();
        carro.setValorDiaria(new BigDecimal("100.00"));
        cliente = new Cliente();
        cliente.setLogin("cli");
    }

    @Test
    void calculaValorPelasDiarias() {
        PedidoAluguel p = new PedidoAluguel(cliente, carro, hoje, hoje.plusDays(3));
        assertEquals(new BigDecimal("300.00"), p.getValorEstimado());
        assertEquals(StatusPedido.PENDENTE, p.getStatus());
    }

    @Test
    void mesmoDiaContaUmaDiaria() {
        PedidoAluguel p = new PedidoAluguel(cliente, carro, hoje, hoje);
        assertEquals(new BigDecimal("100.00"), p.getValorEstimado());
    }

    @Test
    void naoAceitaDataFimAntesDoInicio() {
        assertThrows(RegraNegocioException.class, () -> new PedidoAluguel(cliente, carro, hoje, hoje.minusDays(1)));
    }

    @Test
    void cancelaPedidoPendente() {
        PedidoAluguel p = new PedidoAluguel(cliente, carro, hoje, hoje.plusDays(1));
        p.cancelar();
        assertEquals(StatusPedido.CANCELADO, p.getStatus());
        assertThrows(RegraNegocioException.class, p::cancelar);
    }

    @Test
    void clienteNaoAlteraPedidoCancelado() {
        PedidoAluguel p = new PedidoAluguel(cliente, carro, hoje, hoje.plusDays(1));
        p.cancelar();
        assertThrows(RegraNegocioException.class, () -> p.alterarPeloCliente(carro, hoje, hoje.plusDays(2)));
    }

    @Test
    void clienteNaoPodeTerMaisDeTresRendimentos() {
        for (int i = 0; i < 3; i++) cliente.adicionarRendimento(new Rendimento("E" + i, BigDecimal.TEN));
        assertThrows(RegraNegocioException.class, () -> cliente.adicionarRendimento(new Rendimento("E4", BigDecimal.TEN)));
    }
}
