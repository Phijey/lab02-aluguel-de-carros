package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.AgenteResponse;
import br.pucminas.aluguelcarros.model.Perfil;
import br.pucminas.aluguelcarros.service.AgenteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agentes")
@PreAuthorize("hasRole('AGENTE')")
public class AgenteController {

    private final AgenteService agenteService;

    public AgenteController(AgenteService agenteService) {
        this.agenteService = agenteService;
    }

    @GetMapping
    public List<AgenteResponse> listar(@RequestParam(required = false) Perfil perfil) {
        return agenteService.listar(perfil).stream().map(AgenteResponse::from).toList();
    }
}
