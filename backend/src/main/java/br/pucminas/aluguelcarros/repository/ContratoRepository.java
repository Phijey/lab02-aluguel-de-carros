package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    List<Contrato> findAllByOrderByIdDesc();
    List<Contrato> findByPedidoClienteLoginOrderByIdDesc(String login);
    Optional<Contrato> findByPedidoId(Long pedidoId);
}
