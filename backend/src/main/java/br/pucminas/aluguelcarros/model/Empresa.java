package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresas")
@PrimaryKeyJoinColumn(name = "id")
public class Empresa extends Agente {
    public Empresa() {
        setPerfil(Perfil.EMPRESA);
    }
}
