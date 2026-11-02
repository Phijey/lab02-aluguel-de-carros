package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.*;
import br.pucminas.aluguelcarros.service.AgenteService;
import br.pucminas.aluguelcarros.service.AuthService;
import br.pucminas.aluguelcarros.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ClienteService clienteService;
    private final AgenteService agenteService;

    public AuthController(AuthService authService, ClienteService clienteService, AgenteService agenteService) {
        this.authService = authService;
        this.clienteService = clienteService;
        this.agenteService = agenteService;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/registro/cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse registrarCliente(@RequestBody @Valid ClienteRequest req) {
        return ClienteResponse.from(clienteService.criar(req));
    }

    @PostMapping("/registro/agente")
    @ResponseStatus(HttpStatus.CREATED)
    public AgenteResponse registrarAgente(@RequestBody @Valid AgenteRequest req) {
        return AgenteResponse.from(agenteService.criar(req));
    }

    @GetMapping("/me")
    public UsuarioResponse me(@AuthenticationPrincipal Jwt jwt) {
        return UsuarioResponse.from(authService.buscarPorLogin(jwt.getSubject()));
    }
}
