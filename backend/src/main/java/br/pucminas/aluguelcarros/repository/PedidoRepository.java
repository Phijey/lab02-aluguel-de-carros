package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.PedidoAluguel;
import br.pucminas.aluguelcarros.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PedidoRepository extends JpaRepository<PedidoAluguel, Long> {

    List<PedidoAluguel> findByClienteLoginOrderByCriadoEmDesc(String login);

    List<PedidoAluguel> findAllByOrderByCriadoEmDesc();
    List<PedidoAluguel> findByStatusOrderByCriadoEmDesc(StatusPedido status);

    boolean existsByClienteLoginAndStatusIn(String login, Collection<StatusPedido> status);
    boolean existsByAutomovelId(Long automovelId);

    @Query("""
            select count(p) > 0 from PedidoAluguel p
            where p.automovel.id = :automovelId and p.status in :status
              and p.dataInicio <= :fim and p.dataFim >= :inicio
              and p.id <> :ignorar
            """)
    boolean existeConflito(@Param("automovelId") Long automovelId, @Param("inicio") LocalDate inicio,
                           @Param("fim") LocalDate fim, @Param("status") Collection<StatusPedido> status,
                           @Param("ignorar") Long ignorar);
}
