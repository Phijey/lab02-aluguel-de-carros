package br.pucminas.aluguelcarros;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTest {

    @Autowired
    MockMvc mvc;

    private String token(String login, String senha) throws Exception {
        String json = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"" + login + "\",\"senha\":\"" + senha + "\"}"))
                .andReturn().getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(json, "$.token");
    }

    @Test
    void crudDoProprioCliente() throws Exception {
        mvc.perform(post("/api/auth/registro/cliente").contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"ana\",\"senha\":\"123456\",\"nome\":\"Ana\",\"rg\":\"MG9\",\"cpf\":\"99988877766\"}"))
                .andExpect(status().isCreated());
        String t = token("ana", "123456");

        mvc.perform(get("/api/clientes/me").header("Authorization", t))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana"));

        mvc.perform(put("/api/clientes/me").header("Authorization", t).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"ana\",\"nome\":\"Ana Paula\",\"rg\":\"MG9\",\"cpf\":\"99988877766\",\"profissao\":\"Médica\","
                                + "\"rendimentos\":[{\"entidadeEmpregadora\":\"Hospital\",\"valor\":9000}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Paula"))
                .andExpect(jsonPath("$.rendimentos.length()").value(1));

        mvc.perform(get("/api/clientes").header("Authorization", t)).andExpect(status().isForbidden());

        mvc.perform(delete("/api/clientes/me").header("Authorization", t)).andExpect(status().isNoContent());
    }

    @Test
    void agenteListaClientes() throws Exception {
        mvc.perform(post("/api/auth/registro/agente").contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"emp\",\"senha\":\"123456\",\"nome\":\"Emp\",\"cnpj\":\"11111111000111\",\"tipo\":\"EMPRESA\"}"))
                .andExpect(status().isCreated());
        mvc.perform(get("/api/clientes").header("Authorization", token("emp", "123456")))
                .andExpect(status().isOk());
    }
}
