package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.AvaliacaoRequest;
import br.pucminas.aluguelcarros.dto.PedidoAgenteRequest;
import br.pucminas.aluguelcarros.dto.PedidoRequest;
import br.pucminas.aluguelcarros.exception.RecursoNaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.*;
import br.pucminas.aluguelcarros.repository.PedidoRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class PedidoService {

    private static final Set<StatusPedido> OCUPAM_AUTOMOVEL = Set.of(StatusPedido.APROVADO, StatusPedido.CONTRATADO);

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final AutomovelService automovelService;
    private final AgenteService agenteService;

    public PedidoService(PedidoRepository pedidoRepository, ClienteService clienteService,
                         AutomovelService automovelService, AgenteService agenteService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.automovelService = automovelService;
        this.agenteService = agenteService;
    }

    @Transactional
    public PedidoAluguel criar(String loginCliente, PedidoRequest req) {
        if (req.dataInicio().isBefore(LocalDate.now()))
            throw new RegraNegocioException("A data inicial não pode estar no passado");
        Cliente cliente = clienteService.buscarPorLogin(loginCliente);
        Automovel automovel = automovelService.buscar(req.automovelId());
        verificarDisponibilidade(automovel, req.dataInicio(), req.dataFim(), 0L);
        return pedidoRepository.save(new PedidoAluguel(cliente, automovel, req.dataInicio(), req.dataFim()));
    }

    public List<PedidoAluguel> meusPedidos(String loginCliente) {
        return pedidoRepository.findByClienteLoginOrderByCriadoEmDesc(loginCliente);
    }

    public PedidoAluguel buscar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));
    }

    public PedidoAluguel buscarDoCliente(Long id, String loginCliente) {
        PedidoAluguel p = buscar(id);
        if (!p.pertenceA(loginCliente)) throw new AccessDeniedException("Pedido de outro cliente");
        return p;
    }

    @Transactional
    public PedidoAluguel alterarPeloCliente(Long id, String loginCliente, PedidoRequest req) {
        PedidoAluguel p = buscarDoCliente(id, loginCliente);
        Automovel automovel = automovelService.buscar(req.automovelId());
        verificarDisponibilidade(automovel, req.dataInicio(), req.dataFim(), p.getId());
        p.alterarPeloCliente(automovel, req.dataInicio(), req.dataFim());
        return p;
    }

    @Transactional
    public PedidoAluguel cancelar(Long id, String loginCliente) {
        PedidoAluguel p = buscarDoCliente(id, loginCliente);
        p.cancelar();
        return p;
    }

    public List<PedidoAluguel> listar(StatusPedido status) {
        if (status == null) return pedidoRepository.findAllByOrderByCriadoEmDesc();
        return pedidoRepository.findByStatusOrderByCriadoEmDesc(status);
    }

    public PedidoAluguel buscarParaUsuario(Long id, String login, boolean agente) {
        return agente ? buscar(id) : buscarDoCliente(id, login);
    }

    @Transactional
    public PedidoAluguel iniciarAnalise(Long id, String loginAgente) {
        PedidoAluguel p = buscar(id);
        p.iniciarAnalise(agenteService.buscarPorLogin(loginAgente));
        return p;
    }

    @Transactional
    public PedidoAluguel alterarPeloAgente(Long id, PedidoAgenteRequest req) {
        PedidoAluguel p = buscar(id);
        verificarDisponibilidade(p.getAutomovel(), req.dataInicio(), req.dataFim(), p.getId());
        p.alterarPeloAgente(req.dataInicio(), req.dataFim(), req.valorEstimado());
        return p;
    }

    @Transactional
    public PedidoAluguel avaliar(Long id, String loginAgente, AvaliacaoRequest req) {
        PedidoAluguel p = buscar(id);
        if (req.aprovado())
            verificarDisponibilidade(p.getAutomovel(), p.getDataInicio(), p.getDataFim(), p.getId());
        p.avaliar(agenteService.buscarPorLogin(loginAgente), req.aprovado(), req.parecer());
        return p;
    }

    private void verificarDisponibilidade(Automovel a, LocalDate inicio, LocalDate fim, Long ignorar) {
        if (pedidoRepository.existeConflito(a.getId(), inicio, fim, OCUPAM_AUTOMOVEL, ignorar))
            throw new RegraNegocioException("O automóvel já está reservado nesse período");
    }
}
