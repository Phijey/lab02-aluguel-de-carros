package br.pucminas.aluguelcarros;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FluxoPedidoTest {

    @Autowired
    MockMvc mvc;

    private ResultActions postJson(String url, String token, String body) throws Exception {
        var req = post(url).contentType(MediaType.APPLICATION_JSON).content(body);
        if (token != null) req.header("Authorization", token);
        return mvc.perform(req);
    }

    private String token(String login) throws Exception {
        String json = postJson("/api/auth/login", null, "{\"login\":\"" + login + "\",\"senha\":\"123456\"}")
                .andReturn().getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(json, "$.token");
    }

    private Integer id(ResultActions r) throws Exception {
        return JsonPath.read(r.andReturn().getResponse().getContentAsString(), "$.id");
    }

    @Test
    void pedidoAvaliadoEContratadoComCredito() throws Exception {
        postJson("/api/auth/registro/agente", null, "{\"login\":\"loc\",\"senha\":\"123456\",\"nome\":\"Loc\",\"cnpj\":\"22222222000122\",\"tipo\":\"EMPRESA\"}");
        postJson("/api/auth/registro/agente", null, "{\"login\":\"bco\",\"senha\":\"123456\",\"nome\":\"Bco\",\"cnpj\":\"33333333000133\",\"tipo\":\"BANCO\"}");
        postJson("/api/auth/registro/cliente", null, "{\"login\":\"carla\",\"senha\":\"123456\",\"nome\":\"Carla\",\"rg\":\"MG7\",\"cpf\":\"44455566677\"}");
        String empresa = token("loc"), banco = token("bco"), cliente = token("carla");

        Integer carro = id(postJson("/api/automoveis", empresa,
                "{\"matricula\":\"T-1\",\"ano\":2024,\"marca\":\"Fiat\",\"modelo\":\"Mobi\",\"placa\":\"TST1A11\",\"valorDiaria\":100}")
                .andExpect(status().isCreated()));

        postJson("/api/automoveis", cliente,
                "{\"matricula\":\"T-2\",\"ano\":2024,\"marca\":\"X\",\"modelo\":\"Y\",\"placa\":\"TST2B22\",\"valorDiaria\":100}")
                .andExpect(status().isForbidden());

        LocalDate ini = LocalDate.now().plusDays(1);
        Integer pedido = id(postJson("/api/pedidos", cliente,
                "{\"automovelId\":" + carro + ",\"dataInicio\":\"" + ini + "\",\"dataFim\":\"" + ini.plusDays(4) + "\"}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.valorEstimado").value(400)));

        mvc.perform(patch("/api/pedidos/" + pedido + "/analise").header("Authorization", empresa))
                .andExpect(jsonPath("$.status").value("EM_ANALISE"));

        mvc.perform(patch("/api/pedidos/" + pedido + "/avaliacao").header("Authorization", empresa)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"aprovado\":true,\"parecer\":\"Renda compatível\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APROVADO"));

        postJson("/api/contratos", banco, "{\"pedidoId\":" + pedido + ",\"tipoProprietario\":\"BANCO\","
                + "\"credito\":{\"valor\":400,\"taxaJuros\":1.5,\"numeroParcelas\":4}}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pedido.status").value("CONTRATADO"))
                .andExpect(jsonPath("$.credito.banco").value("Bco"))
                .andExpect(jsonPath("$.pedido.automovel.proprietario").value("Bco"));

        mvc.perform(get("/api/contratos").header("Authorization", cliente))
                .andExpect(jsonPath("$.length()").value(1));

        mvc.perform(patch("/api/pedidos/" + pedido + "/cancelar").header("Authorization", cliente))
                .andExpect(status().isBadRequest());
    }

    @Test
    void empresaNaoConcedeCreditoSemBanco() throws Exception {
        postJson("/api/auth/registro/agente", null, "{\"login\":\"loc2\",\"senha\":\"123456\",\"nome\":\"Loc2\",\"cnpj\":\"44444444000144\",\"tipo\":\"EMPRESA\"}");
        postJson("/api/auth/registro/cliente", null, "{\"login\":\"davi\",\"senha\":\"123456\",\"nome\":\"Davi\",\"rg\":\"MG8\",\"cpf\":\"55566677788\"}");
        String empresa = token("loc2"), cliente = token("davi");

        Integer carro = id(postJson("/api/automoveis", empresa,
                "{\"matricula\":\"T-3\",\"ano\":2023,\"marca\":\"VW\",\"modelo\":\"Up\",\"placa\":\"TST3C33\",\"valorDiaria\":90}"));
        LocalDate ini = LocalDate.now().plusDays(2);
        Integer pedido = id(postJson("/api/pedidos", cliente,
                "{\"automovelId\":" + carro + ",\"dataInicio\":\"" + ini + "\",\"dataFim\":\"" + ini.plusDays(1) + "\"}"));

        mvc.perform(patch("/api/pedidos/" + pedido + "/avaliacao").header("Authorization", empresa)
                .contentType(MediaType.APPLICATION_JSON).content("{\"aprovado\":true,\"parecer\":\"ok\"}"));

        postJson("/api/contratos", empresa, "{\"pedidoId\":" + pedido + ",\"tipoProprietario\":\"CLIENTE\","
                + "\"credito\":{\"valor\":90,\"taxaJuros\":2,\"numeroParcelas\":2}}")
                .andExpect(status().isBadRequest());

        postJson("/api/contratos", empresa, "{\"pedidoId\":" + pedido + ",\"tipoProprietario\":\"CLIENTE\"}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pedido.automovel.proprietario").value("Davi"));
    }
}
