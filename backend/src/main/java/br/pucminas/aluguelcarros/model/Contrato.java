package br.pucminas.aluguelcarros.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    private PedidoAluguel pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agente_id")
    private Agente agente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proprietario", nullable = false)
    private TipoProprietario tipoProprietario;

    @Column(name = "data_assinatura", nullable = false)
    private LocalDate dataAssinatura;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contrato_credito_id")
    private ContratoCredito contratoCredito;

    protected Contrato() {}

    public Contrato(PedidoAluguel pedido, Agente agente, TipoProprietario tipo, ContratoCredito credito) {
        pedido.marcarContratado();
        this.pedido = pedido;
        this.agente = agente;
        this.tipoProprietario = tipo;
        this.contratoCredito = credito;
        this.dataAssinatura = LocalDate.now();
    }

    public Long getId() { return id; }
    public PedidoAluguel getPedido() { return pedido; }
    public Agente getAgente() { return agente; }
    public TipoProprietario getTipoProprietario() { return tipoProprietario; }
    public LocalDate getDataAssinatura() { return dataAssinatura; }
    public ContratoCredito getContratoCredito() { return contratoCredito; }
}
