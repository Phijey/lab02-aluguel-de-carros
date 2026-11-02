package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.AvaliacaoRequest;
import br.pucminas.aluguelcarros.dto.PedidoAgenteRequest;
import br.pucminas.aluguelcarros.dto.PedidoRequest;
import br.pucminas.aluguelcarros.dto.PedidoResponse;
import br.pucminas.aluguelcarros.model.StatusPedido;
import br.pucminas.aluguelcarros.security.UsuarioLogado;
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

    @GetMapping
    @PreAuthorize("hasRole('AGENTE')")
    @Transactional(readOnly = true)
    public List<PedidoResponse> listar(@RequestParam(required = false) StatusPedido status) {
        return pedidoService.listar(status).stream().map(PedidoResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PedidoResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return PedidoResponse.from(pedidoService.buscarParaUsuario(id, jwt.getSubject(), UsuarioLogado.isAgente(jwt)));
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

    @PatchMapping("/{id}/analise")
    @PreAuthorize("hasRole('AGENTE')")
    @Transactional
    public PedidoResponse iniciarAnalise(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return PedidoResponse.from(pedidoService.iniciarAnalise(id, jwt.getSubject()));
    }

    @PutMapping("/{id}/agente")
    @PreAuthorize("hasRole('AGENTE')")
    @Transactional
    public PedidoResponse alterarPeloAgente(@PathVariable Long id, @RequestBody @Valid PedidoAgenteRequest req) {
        return PedidoResponse.from(pedidoService.alterarPeloAgente(id, req));
    }

    @PatchMapping("/{id}/avaliacao")
    @PreAuthorize("hasRole('AGENTE')")
    @Transactional
    public PedidoResponse avaliar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt, @RequestBody @Valid AvaliacaoRequest req) {
        return PedidoResponse.from(pedidoService.avaliar(id, jwt.getSubject(), req));
    }
}
