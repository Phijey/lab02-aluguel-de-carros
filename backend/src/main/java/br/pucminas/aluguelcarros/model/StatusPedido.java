package br.pucminas.aluguelcarros.model;

public enum StatusPedido {
    PENDENTE,
    EM_ANALISE,
    APROVADO,
    REPROVADO,
    CANCELADO,
    CONTRATADO;

    public boolean podeSerCancelado() {
        return this == PENDENTE || this == EM_ANALISE;
    }
}
