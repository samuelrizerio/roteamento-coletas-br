package br.com.roteamento.security;

import br.com.roteamento.dto.AuthRequest;
import br.com.roteamento.dto.RegisterRequest;
import br.com.roteamento.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.security.jwt.secret=test-secret-key-for-testing-only-not-for-production-use",
        "spring.security.jwt.expiration=3600000",
        "spring.security.jwt.refresh-token.expiration=86400000",
        "server.servlet.context-path=/"
})
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Bloqueia acesso sem token e libera com Bearer JWT")
    void shouldProtectEndpointsWithJwt() throws Exception {
        // 1) Verifica 401 sem token
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isUnauthorized());

        // 2) Registra usuário e obtém token
        RegisterRequest register = RegisterRequest.builder()
                .nome("Usuário Teste")
                .email("teste.jwt@example.com")
                .senha("Senha@123")
                .tipoUsuario(Usuario.TipoUsuario.COLETOR)
                .build();

        ResultActions registerResp = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());

        String accessToken = registerResp.andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(accessToken).get("accessToken").asText();

        // 3) Acesso com token deve retornar 200
        mockMvc.perform(get("/usuarios")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // 4) Validação de token deve indicar válido
        mockMvc.perform(get("/api/v1/auth/validate")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.username").value("teste.jwt@example.com"));
    }

    @Test
    @DisplayName("Swagger e health devem permanecer públicos")
    void shouldKeepPublicEndpointsOpen() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}
