package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.AutomovelRequest;
import br.pucminas.aluguelcarros.exception.RecursoNaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.model.TipoProprietario;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutomovelService {

    private final AutomovelRepository automovelRepository;
    private final PedidoRepository pedidoRepository;

    public AutomovelService(AutomovelRepository automovelRepository, PedidoRepository pedidoRepository) {
        this.automovelRepository = automovelRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Automovel> listar() {
        return automovelRepository.findAllByOrderByMarcaAscModeloAsc();
    }

    public Automovel buscar(Long id) {
        return automovelRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Automóvel não encontrado"));
    }

    @Transactional
    public Automovel criar(AutomovelRequest req, Agente dono) {
        String placa = normalizarPlaca(req.placa());
        if (automovelRepository.existsByPlaca(placa)) throw new RegraNegocioException("Já existe automóvel com essa placa");
        if (automovelRepository.existsByMatricula(req.matricula())) throw new RegraNegocioException("Já existe automóvel com essa matrícula");
        Automovel a = new Automovel();
        preencher(a, req, placa);
        a.registrarPropriedade(dono instanceof Banco ? TipoProprietario.BANCO : TipoProprietario.EMPRESA, dono);
        return automovelRepository.save(a);
    }

    @Transactional
    public Automovel atualizar(Long id, AutomovelRequest req) {
        Automovel a = buscar(id);
        String placa = normalizarPlaca(req.placa());
        if (automovelRepository.existsByPlacaAndIdNot(placa, id)) throw new RegraNegocioException("Já existe automóvel com essa placa");
        if (automovelRepository.existsByMatriculaAndIdNot(req.matricula(), id)) throw new RegraNegocioException("Já existe automóvel com essa matrícula");
        preencher(a, req, placa);
        return a;
    }

    @Transactional
    public void excluir(Long id) {
        Automovel a = buscar(id);
        if (pedidoRepository.existsByAutomovelId(id))
            throw new RegraNegocioException("Automóvel possui pedidos e não pode ser excluído");
        automovelRepository.delete(a);
    }

    private void preencher(Automovel a, AutomovelRequest req, String placa) {
        a.setMatricula(req.matricula());
        a.setAno(req.ano());
        a.setMarca(req.marca());
        a.setModelo(req.modelo());
        a.setPlaca(placa);
        a.setValorDiaria(req.valorDiaria());
    }

    private String normalizarPlaca(String placa) {
        return placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }
}
