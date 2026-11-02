package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "id")
public class Cliente extends Usuario {

    public static final int MAX_RENDIMENTOS = 3;

    @Column(nullable = false)
    private String rg;
    @Column(nullable = false, unique = true)
    private String cpf;
    private String profissao;

    @Embedded
    private Endereco endereco;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rendimento> rendimentos = new ArrayList<>();

    public Cliente() {
        setPerfil(Perfil.CLIENTE);
    }

    public void adicionarRendimento(Rendimento r) {
        if (rendimentos.size() >= MAX_RENDIMENTOS)
            throw new RegraNegocioException("O cliente pode ter no máximo " + MAX_RENDIMENTOS + " rendimentos");
        r.setCliente(this);
        rendimentos.add(r);
    }

    public void substituirRendimentos(List<Rendimento> novos) {
        rendimentos.clear();
        for (Rendimento r : novos) adicionarRendimento(r);
    }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    public List<Rendimento> getRendimentos() { return rendimentos; }
}
