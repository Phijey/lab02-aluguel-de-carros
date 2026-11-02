package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.AgenteRequest;
import br.pucminas.aluguelcarros.exception.RecursoNaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.*;
import br.pucminas.aluguelcarros.repository.AgenteRepository;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgenteService {

    private final AgenteRepository agenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AgenteService(AgenteRepository agenteRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.agenteRepository = agenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Agente criar(AgenteRequest req) {
        if (req.tipo() == Perfil.CLIENTE)
            throw new RegraNegocioException("O tipo do agente deve ser EMPRESA ou BANCO");
        if (usuarioRepository.existsByLogin(req.login()))
            throw new RegraNegocioException("Já existe um usuário com esse login");
        String cnpj = req.cnpj().replaceAll("\\D", "");
        if (agenteRepository.existsByCnpj(cnpj))
            throw new RegraNegocioException("Já existe um agente com esse CNPJ");

        Agente a = req.tipo() == Perfil.BANCO ? new Banco() : new Empresa();
        a.setLogin(req.login());
        a.setSenha(passwordEncoder.encode(req.senha()));
        a.setNome(req.nome());
        a.setCnpj(cnpj);
        return agenteRepository.save(a);
    }

    public List<Agente> listar(Perfil perfil) {
        if (perfil == null) return agenteRepository.findAll();
        return agenteRepository.findByPerfilOrderByNomeAsc(perfil);
    }

    public Agente buscarPorLogin(String login) {
        return agenteRepository.findByLogin(login)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agente não encontrado"));
    }
}
