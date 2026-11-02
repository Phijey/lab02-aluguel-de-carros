package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.AutomovelRequest;
import br.pucminas.aluguelcarros.dto.AutomovelResponse;
import br.pucminas.aluguelcarros.service.AgenteService;
import br.pucminas.aluguelcarros.service.AutomovelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automoveis")
public class AutomovelController {

    private final AutomovelService automovelService;
    private final AgenteService agenteService;

    public AutomovelController(AutomovelService automovelService, AgenteService agenteService) {
        this.automovelService = automovelService;
        this.agenteService = agenteService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<AutomovelResponse> listar() {
        return automovelService.listar().stream().map(AutomovelResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public AutomovelResponse buscar(@PathVariable Long id) {
        return AutomovelResponse.from(automovelService.buscar(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('AGENTE')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public AutomovelResponse criar(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid AutomovelRequest req) {
        return AutomovelResponse.from(automovelService.criar(req, agenteService.buscarPorLogin(jwt.getSubject())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AGENTE')")
    @Transactional
    public AutomovelResponse atualizar(@PathVariable Long id, @RequestBody @Valid AutomovelRequest req) {
        return AutomovelResponse.from(automovelService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AGENTE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        automovelService.excluir(id);
    }
}
