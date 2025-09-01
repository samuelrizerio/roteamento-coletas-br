package br.com.roteamento.controller;

import br.com.roteamento.dto.ColetaDTO;
import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Material;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.repository.ColetaRepository;
import br.com.roteamento.repository.MaterialRepository;
import br.com.roteamento.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES DE INTEGRAÇÃO PARA CONTROLLER DE COLETAS
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Testes de integração completos
 * - Validação de endpoints REST
 * - Testes de validação de dados
 * - Cobertura de cenários de erro
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ColetaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ColetaRepository coletaRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Material materialTeste;
    private Usuario usuarioTeste;
    private Coleta coletaTeste;

    @BeforeEach
    void setUp() {
        // Limpar dados de teste
        coletaRepository.deleteAll();
        materialRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Criar dados de teste
        materialTeste = new Material();
        materialTeste.setNome("Papel");
        materialTeste.setValorPorQuilo(new BigDecimal("2.50"));
        materialTeste = materialRepository.save(materialTeste);

        usuarioTeste = new Usuario();
        usuarioTeste.setNome("João Teste");
        usuarioTeste.setEmail("joao@teste.com");
        usuarioTeste.setSenha("senha123");
        usuarioTeste = usuarioRepository.save(usuarioTeste);

        coletaTeste = new Coleta();
        coletaTeste.setUsuario(usuarioTeste);
        coletaTeste.setMaterial(materialTeste);
        coletaTeste.setQuantidade(new BigDecimal("10.0"));
        coletaTeste.setEndereco("Rua Teste, 123");
        coletaTeste.setLatitude(-19.9167);
        coletaTeste.setLongitude(-43.9345);
        coletaTeste = coletaRepository.save(coletaTeste);
    }

    /**
     * TESTE: Criar coleta com sucesso
     * 
     * MELHORIAS:
     * - Validação de criação
     * - Verificação de status HTTP
     * - Validação de dados retornados
     */
    @Test
    void testCriarColeta_Sucesso() throws Exception {
        // Given
        ColetaDTO novaColeta = new ColetaDTO();
        novaColeta.setUsuarioId(usuarioTeste.getId());
        novaColeta.setMaterialId(materialTeste.getId());
        novaColeta.setPesoEstimado(new BigDecimal("15.0"));
        novaColeta.setEndereco("Rua Nova, 456");
        novaColeta.setLatitude(-19.9267);
        novaColeta.setLongitude(-43.9445);

        // When & Then
        mockMvc.perform(post("/api/v1/coletas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novaColeta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.pesoEstimado").value(15.0))
                .andExpect(jsonPath("$.endereco").value("Rua Nova, 456"));
    }

    /**
     * TESTE: Buscar coleta por ID
     * 
     * MELHORIAS:
     * - Validação de busca
     * - Verificação de dados
     * - Teste de endpoint GET
     */
    @Test
    void testBuscarColetaPorId_Sucesso() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/coletas/{id}", coletaTeste.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(coletaTeste.getId()))
                .andExpect(jsonPath("$.pesoEstimado").value(10.0))
                .andExpect(jsonPath("$.endereco").value("Rua Teste, 123"));
    }

    /**
     * TESTE: Buscar coleta inexistente
     * 
     * MELHORIAS:
     * - Teste de erro 404
     * - Validação de tratamento de erro
     * - Cobertura de casos de erro
     */
    @Test
    void testBuscarColetaPorId_NaoEncontrada() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/coletas/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    /**
     * TESTE: Listar todas as coletas
     * 
     * MELHORIAS:
     * - Validação de listagem
     * - Verificação de quantidade
     * - Teste de endpoint GET
     */
    @Test
    void testListarTodasColetas_Sucesso() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/coletas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(coletaTeste.getId()));
    }

    /**
     * TESTE: Atualizar coleta
     * 
     * MELHORIAS:
     * - Validação de atualização
     * - Verificação de dados modificados
     * - Teste de endpoint PUT
     */
    @Test
    void testAtualizarColeta_Sucesso() throws Exception {
        // Given
        ColetaDTO coletaAtualizada = new ColetaDTO();
        coletaAtualizada.setId(coletaTeste.getId());
        coletaAtualizada.setUsuarioId(usuarioTeste.getId());
        coletaAtualizada.setMaterialId(materialTeste.getId());
        coletaAtualizada.setPesoEstimado(new BigDecimal("20.0"));
        coletaAtualizada.setEndereco("Rua Atualizada, 789");

        // When & Then
        mockMvc.perform(put("/api/v1/coletas/{id}", coletaTeste.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(coletaAtualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pesoEstimado").value(20.0))
                .andExpect(jsonPath("$.endereco").value("Rua Atualizada, 789"));
    }

    /**
     * TESTE: Excluir coleta
     * 
     * MELHORIAS:
     * - Validação de exclusão
     * - Verificação de status HTTP
     * - Teste de endpoint DELETE
     */
    @Test
    void testExcluirColeta_Sucesso() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/coletas/{id}", coletaTeste.getId()))
                .andExpect(status().isNoContent());

        // Verificar se foi excluída
        mockMvc.perform(get("/api/v1/coletas/{id}", coletaTeste.getId()))
                .andExpect(status().isNotFound());
    }

    /**
     * TESTE: Validação de dados inválidos
     * 
     * MELHORIAS:
     * - Teste de validação
     * - Verificação de erro 400
     * - Cobertura de casos de erro
     */
    @Test
    void testCriarColeta_DadosInvalidos_RetornaErro() throws Exception {
        // Given
        ColetaDTO coletaInvalida = new ColetaDTO();
        // Dados obrigatórios não preenchidos

        // When & Then
        mockMvc.perform(post("/api/v1/coletas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(coletaInvalida)))
                .andExpect(status().isBadRequest());
    }
}
