package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.AutomovelResponse;
import br.pucminas.aluguelcarros.service.AutomovelService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automoveis")
public class AutomovelController {

    private final AutomovelService automovelService;

    public AutomovelController(AutomovelService automovelService) {
        this.automovelService = automovelService;
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
}
