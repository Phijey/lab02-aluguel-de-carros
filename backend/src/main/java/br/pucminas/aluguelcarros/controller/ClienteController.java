package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.ClienteRequest;
import br.pucminas.aluguelcarros.dto.ClienteResponse;
import br.pucminas.aluguelcarros.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @PreAuthorize("hasRole('AGENTE')")
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteService.listar().stream().map(ClienteResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('AGENTE')")
    @Transactional(readOnly = true)
    public ClienteResponse buscar(@PathVariable Long id) {
        return ClienteResponse.from(clienteService.buscar(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional(readOnly = true)
    public ClienteResponse meusDados(@AuthenticationPrincipal Jwt jwt) {
        return ClienteResponse.from(clienteService.buscarPorLogin(jwt.getSubject()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    public ClienteResponse atualizar(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid ClienteRequest req) {
        return ClienteResponse.from(clienteService.atualizar(jwt.getSubject(), req));
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@AuthenticationPrincipal Jwt jwt) {
        clienteService.excluir(jwt.getSubject());
    }
}
