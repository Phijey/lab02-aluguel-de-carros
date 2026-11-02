package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.ContratoRequest;
import br.pucminas.aluguelcarros.exception.RecursoNaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.*;
import br.pucminas.aluguelcarros.repository.AgenteRepository;
import br.pucminas.aluguelcarros.repository.ContratoRepository;
import org.hibernate.Hibernate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final PedidoService pedidoService;
    private final AgenteService agenteService;
    private final AgenteRepository agenteRepository;

    public ContratoService(ContratoRepository contratoRepository, PedidoService pedidoService,
                           AgenteService agenteService, AgenteRepository agenteRepository) {
        this.contratoRepository = contratoRepository;
        this.pedidoService = pedidoService;
        this.agenteService = agenteService;
        this.agenteRepository = agenteRepository;
    }

    @Transactional
    public Contrato executar(String loginAgente, ContratoRequest req) {
        Agente agente = (Agente) Hibernate.unproxy(agenteService.buscarPorLogin(loginAgente));
        PedidoAluguel pedido = pedidoService.buscar(req.pedidoId());

        ContratoCredito credito = null;
        if (req.credito() != null) {
            Banco banco = escolherBanco(agente, req.credito().bancoId());
            credito = new ContratoCredito(banco, req.credito().valor(), req.credito().taxaJuros(), req.credito().numeroParcelas());
        }

        Automovel automovel = pedido.getAutomovel();
        switch (req.tipoProprietario()) {
            case CLIENTE -> automovel.registrarPropriedade(TipoProprietario.CLIENTE, pedido.getCliente());
            case BANCO -> {
                if (credito == null)
                    throw new RegraNegocioException("Para registrar o automóvel em nome do banco é preciso um contrato de crédito");
                automovel.registrarPropriedade(TipoProprietario.BANCO, credito.getBanco());
            }
            case EMPRESA -> {
                if (agente instanceof Empresa) automovel.registrarPropriedade(TipoProprietario.EMPRESA, agente);
                else if (automovel.getTipoProprietario() != TipoProprietario.EMPRESA)
                    throw new RegraNegocioException("Não há empresa para registrar a propriedade do automóvel");
            }
        }

        return contratoRepository.save(new Contrato(pedido, agente, req.tipoProprietario(), credito));
    }

    private Banco escolherBanco(Agente agente, Long bancoId) {
        if (bancoId == null) {
            if (agente instanceof Banco b) return b;
            throw new RegraNegocioException("Informe o banco que concede o crédito");
        }
        Object a = Hibernate.unproxy(agenteRepository.findById(bancoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Banco não encontrado")));
        if (!(a instanceof Banco b)) throw new RegraNegocioException("O agente informado não é um banco");
        return b;
    }

    public List<Contrato> listar(String login, boolean agente) {
        return agente ? contratoRepository.findAllByOrderByIdDesc() : contratoRepository.findByPedidoClienteLoginOrderByIdDesc(login);
    }

    public Contrato buscar(Long id, String login, boolean agente) {
        Contrato c = contratoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Contrato não encontrado"));
        if (!agente && !c.getPedido().pertenceA(login)) throw new AccessDeniedException("Contrato de outro cliente");
        return c;
    }

    public Contrato buscarPorPedido(Long pedidoId) {
        return contratoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Contrato não encontrado"));
    }
}
