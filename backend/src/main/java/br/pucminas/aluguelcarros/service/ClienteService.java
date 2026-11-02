package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.ClienteRequest;
import br.pucminas.aluguelcarros.dto.RendimentoDTO;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Rendimento;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
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
