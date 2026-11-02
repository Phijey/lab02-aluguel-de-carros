package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "pedidos")
public class PedidoAluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "automovel_id")
    private Automovel automovel;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "valor_estimado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorEstimado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    protected PedidoAluguel() {}

    public PedidoAluguel(Cliente cliente, Automovel automovel, LocalDate inicio, LocalDate fim) {
        this.cliente = cliente;
        this.status = StatusPedido.PENDENTE;
        this.criadoEm = LocalDateTime.now();
        definirPeriodo(automovel, inicio, fim);
    }

    public static long dias(LocalDate inicio, LocalDate fim) {
        return Math.max(1, ChronoUnit.DAYS.between(inicio, fim));
    }

    private void definirPeriodo(Automovel automovel, LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || fim.isBefore(inicio))
            throw new RegraNegocioException("A data final deve ser igual ou posterior à data inicial");
        this.automovel = automovel;
        this.dataInicio = inicio;
        this.dataFim = fim;
        this.valorEstimado = automovel.getValorDiaria().multiply(BigDecimal.valueOf(dias(inicio, fim)));
    }

    public void alterarPeloCliente(Automovel automovel, LocalDate inicio, LocalDate fim) {
        if (status != StatusPedido.PENDENTE)
            throw new RegraNegocioException("Só é possível modificar pedidos pendentes");
        definirPeriodo(automovel, inicio, fim);
        atualizadoEm = LocalDateTime.now();
    }

    public void cancelar() {
        if (!status.podeSerCancelado())
            throw new RegraNegocioException("Este pedido não pode mais ser cancelado (status " + status + ")");
        status = StatusPedido.CANCELADO;
        atualizadoEm = LocalDateTime.now();
    }

    public boolean pertenceA(String login) {
        return cliente.getLogin().equals(login);
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Automovel getAutomovel() { return automovel; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public BigDecimal getValorEstimado() { return valorEstimado; }
    public StatusPedido getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
