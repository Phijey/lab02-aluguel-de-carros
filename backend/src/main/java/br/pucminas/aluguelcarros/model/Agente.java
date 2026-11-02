package br.pucminas.aluguelcarros.model;

import jakarta.persistence.*;

@Entity
@Table(name = "agentes")
@PrimaryKeyJoinColumn(name = "id")
public abstract class Agente extends Usuario {

    @Column(nullable = false, unique = true)
    private String cnpj;

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
}
