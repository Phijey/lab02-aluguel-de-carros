package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgenteRepository extends JpaRepository<Agente, Long> {
    Optional<Agente> findByLogin(String login);
    boolean existsByCnpj(String cnpj);
    List<Agente> findByPerfilOrderByNomeAsc(Perfil perfil);
}
