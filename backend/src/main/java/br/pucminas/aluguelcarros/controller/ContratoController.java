package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.ContratoRequest;
import br.pucminas.aluguelcarros.dto.ContratoResponse;
import br.pucminas.aluguelcarros.security.UsuarioLogado;
import br.pucminas.aluguelcarros.service.ContratoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('AGENTE')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ContratoResponse executar(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid ContratoRequest req) {
        return ContratoResponse.from(contratoService.executar(jwt.getSubject(), req));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<ContratoResponse> listar(@AuthenticationPrincipal Jwt jwt) {
        return contratoService.listar(jwt.getSubject(), UsuarioLogado.isAgente(jwt)).stream().map(ContratoResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ContratoResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ContratoResponse.from(contratoService.buscar(id, jwt.getSubject(), UsuarioLogado.isAgente(jwt)));
    }
}
