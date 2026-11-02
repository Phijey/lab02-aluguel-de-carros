package br.pucminas.aluguelcarros.security;

import br.pucminas.aluguelcarros.model.Perfil;
import br.pucminas.aluguelcarros.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final long expiracaoHoras;

    public TokenService(JwtEncoder encoder, @Value("${app.jwt.expiracao-horas}") long expiracaoHoras) {
        this.encoder = encoder;
        this.expiracaoHoras = expiracaoHoras;
    }

    public String gerar(Usuario usuario) {
        List<String> roles = new ArrayList<>();
        roles.add(usuario.getPerfil().name());
        if (usuario.getPerfil() != Perfil.CLIENTE) roles.add("AGENTE");

        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("aluguelcarros")
                .issuedAt(agora)
                .expiresAt(agora.plus(expiracaoHoras, ChronoUnit.HOURS))
                .subject(usuario.getLogin())
                .claim("id", usuario.getId())
                .claim("nome", usuario.getNome())
                .claim("roles", roles)
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
