package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "contratos_credito")
public class ContratoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "banco_id")
    private Banco banco;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "taxa_juros", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxaJuros;

    @Column(name = "numero_parcelas", nullable = false)
    private Integer numeroParcelas;

    protected ContratoCredito() {}

    public ContratoCredito(Banco banco, BigDecimal valor, BigDecimal taxaJuros, Integer numeroParcelas) {
        if (valor == null || valor.signum() <= 0) throw new RegraNegocioException("Valor do crédito inválido");
        if (numeroParcelas == null || numeroParcelas < 1) throw new RegraNegocioException("Número de parcelas inválido");
        this.banco = banco;
        this.valor = valor;
        this.taxaJuros = taxaJuros;
        this.numeroParcelas = numeroParcelas;
    }

    public BigDecimal valorParcela() {
        BigDecimal i = taxaJuros.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        if (i.signum() == 0) return valor.divide(BigDecimal.valueOf(numeroParcelas), 2, RoundingMode.HALF_UP);
        double fator = Math.pow(1 + i.doubleValue(), numeroParcelas);
        double parcela = valor.doubleValue() * i.doubleValue() * fator / (fator - 1);
        return BigDecimal.valueOf(parcela).setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() { return id; }
    public Banco getBanco() { return banco; }
    public BigDecimal getValor() { return valor; }
    public BigDecimal getTaxaJuros() { return taxaJuros; }
    public Integer getNumeroParcelas() { return numeroParcelas; }
}
