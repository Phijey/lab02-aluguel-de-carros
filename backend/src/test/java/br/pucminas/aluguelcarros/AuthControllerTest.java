package br.pucminas.aluguelcarros;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void registraClienteEFazLogin() throws Exception {
        String cliente = """
                {"login":"maria","senha":"123456","nome":"Maria Souza","rg":"MG123","cpf":"111.222.333-44",
                 "profissao":"Analista","rendimentos":[{"entidadeEmpregadora":"Empresa X","valor":4500}]}
                """;
        mvc.perform(post("/api/auth/registro/cliente").contentType(MediaType.APPLICATION_JSON).content(cliente))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value("11122233344"));

        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"maria\",\"senha\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.perfil").value("CLIENTE"));
    }

    @Test
    void senhaErradaRetorna401() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"ninguem\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void semTokenNaoAcessa() throws Exception {
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void naoAceitaMaisDeTresRendimentos() throws Exception {
        String cliente = """
                {"login":"joao","senha":"123456","nome":"Joao","rg":"MG1","cpf":"22233344455",
                 "rendimentos":[{"entidadeEmpregadora":"A","valor":1},{"entidadeEmpregadora":"B","valor":1},
                 {"entidadeEmpregadora":"C","valor":1},{"entidadeEmpregadora":"D","valor":1}]}
                """;
        mvc.perform(post("/api/auth/registro/cliente").contentType(MediaType.APPLICATION_JSON).content(cliente))
                .andExpect(status().isBadRequest());
    }
}
