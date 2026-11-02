package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByLogin(String login);

    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, Long id);

    List<Cliente> findAllByOrderByNomeAsc();
}
