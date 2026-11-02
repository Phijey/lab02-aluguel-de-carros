package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.PedidoRequest;
import br.pucminas.aluguelcarros.dto.PedidoResponse;
import br.pucminas.aluguelcarros.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public PedidoResponse criar(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid PedidoRequest req) {
        return PedidoResponse.from(pedidoService.criar(jwt.getSubject(), req));
    }

    @GetMapping("/meus")
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional(readOnly = true)
    public List<PedidoResponse> meus(@AuthenticationPrincipal Jwt jwt) {
        return pedidoService.meusPedidos(jwt.getSubject()).stream().map(PedidoResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PedidoResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return PedidoResponse.from(pedidoService.buscarDoCliente(id, jwt.getSubject()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional
    public PedidoResponse alterar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt, @RequestBody @Valid PedidoRequest req) {
        return PedidoResponse.from(pedidoService.alterarPeloCliente(id, jwt.getSubject(), req));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional
    public PedidoResponse cancelar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return PedidoResponse.from(pedidoService.cancelar(id, jwt.getSubject()));
    }
}
