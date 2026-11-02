package br.pucminas.aluguelcarros.config;

import br.pucminas.aluguelcarros.dto.AgenteRequest;
import br.pucminas.aluguelcarros.model.Perfil;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import br.pucminas.aluguelcarros.service.AgenteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AgenteService agenteService;

    public DataSeeder(UsuarioRepository usuarioRepository, AgenteService agenteService) {
        this.usuarioRepository = usuarioRepository;
        this.agenteService = agenteService;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByLogin("locadora"))
            agenteService.criar(new AgenteRequest("locadora", "123456", "Locadora Rodar Bem", "12.345.678/0001-90", Perfil.EMPRESA));
        if (!usuarioRepository.existsByLogin("banco"))
            agenteService.criar(new AgenteRequest("banco", "123456", "Banco Minas Crédito", "98.765.432/0001-10", Perfil.BANCO));
    }
}
