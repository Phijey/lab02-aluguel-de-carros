package br.pucminas.aluguelcarros.config;

import br.pucminas.aluguelcarros.dto.AgenteRequest;
import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Perfil;
import br.pucminas.aluguelcarros.model.TipoProprietario;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import br.pucminas.aluguelcarros.service.AgenteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AgenteService agenteService;
    private final AutomovelRepository automovelRepository;

    public DataSeeder(UsuarioRepository usuarioRepository, AgenteService agenteService, AutomovelRepository automovelRepository) {
        this.usuarioRepository = usuarioRepository;
        this.agenteService = agenteService;
        this.automovelRepository = automovelRepository;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByLogin("locadora"))
            agenteService.criar(new AgenteRequest("locadora", "123456", "Locadora Rodar Bem", "12.345.678/0001-90", Perfil.EMPRESA));
        if (!usuarioRepository.existsByLogin("banco"))
            agenteService.criar(new AgenteRequest("banco", "123456", "Banco Minas Crédito", "98.765.432/0001-10", Perfil.BANCO));

        if (automovelRepository.count() == 0) {
            Agente locadora = agenteService.buscarPorLogin("locadora");
            carro(locadora, "MAT-0001", 2023, "Fiat", "Argo", "RDB1A23", "129.90");
            carro(locadora, "MAT-0002", 2022, "Volkswagen", "Polo", "RDB2B34", "149.90");
            carro(locadora, "MAT-0003", 2024, "Chevrolet", "Onix Plus", "RDB3C45", "159.90");
            carro(locadora, "MAT-0004", 2023, "Toyota", "Corolla", "RDB4D56", "259.90");
            carro(locadora, "MAT-0005", 2024, "Jeep", "Compass", "RDB5E67", "319.90");
        }
    }

    private void carro(Agente dono, String matricula, int ano, String marca, String modelo, String placa, String diaria) {
        Automovel a = new Automovel();
        a.setMatricula(matricula);
        a.setAno(ano);
        a.setMarca(marca);
        a.setModelo(modelo);
        a.setPlaca(placa);
        a.setValorDiaria(new BigDecimal(diaria));
        a.registrarPropriedade(TipoProprietario.EMPRESA, dono);
        automovelRepository.save(a);
    }
}
