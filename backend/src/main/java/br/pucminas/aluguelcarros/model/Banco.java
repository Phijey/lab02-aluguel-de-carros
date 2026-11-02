package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "bancos")
@PrimaryKeyJoinColumn(name = "id")
public class Banco extends Agente {

    public Banco() {
        setPerfil(Perfil.BANCO);
    }
}
