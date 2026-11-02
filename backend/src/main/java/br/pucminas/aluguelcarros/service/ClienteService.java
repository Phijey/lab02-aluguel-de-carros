package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.ClienteRequest;
import br.pucminas.aluguelcarros.dto.RendimentoDTO;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.exception.RecursoNaoEncontradoException;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Rendimento;
import br.pucminas.aluguelcarros.model.StatusPedido;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository,
                          PedidoRepository pedidoRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Cliente criar(ClienteRequest req) {
        if (req.senha() == null || req.senha().length() < 6)
            throw new RegraNegocioException("A senha deve ter pelo menos 6 caracteres");
        if (usuarioRepository.existsByLogin(req.login()))
            throw new RegraNegocioException("Já existe um usuário com esse login");
        String cpf = somenteNumeros(req.cpf());
        if (clienteRepository.existsByCpf(cpf))
            throw new RegraNegocioException("Já existe um cliente com esse CPF");

        Cliente c = new Cliente();
        c.setLogin(req.login());
        c.setSenha(passwordEncoder.encode(req.senha()));
        preencher(c, req, cpf);
        return clienteRepository.save(c);
    }

    public List<Cliente> listar() {
        return clienteRepository.findAllByOrderByNomeAsc();
    }

    public Cliente buscar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    public Cliente buscarPorLogin(String login) {
        return clienteRepository.findByLogin(login)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    @Transactional
    public Cliente atualizar(String login, ClienteRequest req) {
        Cliente c = buscarPorLogin(login);
        String cpf = somenteNumeros(req.cpf());
        if (clienteRepository.existsByCpfAndIdNot(cpf, c.getId()))
            throw new RegraNegocioException("Já existe um cliente com esse CPF");
        if (req.senha() != null && !req.senha().isBlank()) {
            if (req.senha().length() < 6) throw new RegraNegocioException("A senha deve ter pelo menos 6 caracteres");
            c.setSenha(passwordEncoder.encode(req.senha()));
        }
        preencher(c, req, cpf);
        return clienteRepository.save(c);
    }

    @Transactional
    public void excluir(String login) {
        Cliente c = buscarPorLogin(login);
        if (pedidoRepository.existsByClienteLoginAndStatusIn(login, Set.of(StatusPedido.APROVADO, StatusPedido.CONTRATADO)))
            throw new RegraNegocioException("Não é possível excluir a conta com pedidos aprovados ou contratados");
        clienteRepository.delete(c);
    }

    private void preencher(Cliente c, ClienteRequest req, String cpf) {
        c.setNome(req.nome());
        c.setRg(req.rg());
        c.setCpf(cpf);
        c.setProfissao(req.profissao());
        c.setEndereco(req.endereco() == null ? null : req.endereco().toModel());
        List<RendimentoDTO> rs = req.rendimentos() == null ? List.of() : req.rendimentos();
        c.substituirRendimentos(rs.stream().map(r -> new Rendimento(r.entidadeEmpregadora(), r.valor())).toList());
    }

    private String somenteNumeros(String s) {
        return s.replaceAll("\\D", "");
    }
}
