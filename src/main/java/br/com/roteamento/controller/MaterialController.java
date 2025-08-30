package br.com.roteamento.controller;

import br.com.roteamento.dto.MaterialDTO;
import br.com.roteamento.exception.MaterialNaoEncontradoException;
import br.com.roteamento.exception.NomeMaterialJaExisteException;
import br.com.roteamento.model.Material.CategoriaMaterial;
import br.com.roteamento.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import br.com.roteamento.model.Material;
import br.com.roteamento.repository.MaterialRepository;

/**
 * Controlador REST para gerenciamento de materiais
 * 
 * CONCEITO DIDÁTICO - REST (Representational State Transfer):
 * - Arquitetura para sistemas distribuídos
 * - Baseada em recursos (resources) identificados por URIs
 * - Usa métodos HTTP padrão (GET, POST, PUT, DELETE)
 * - Stateless: cada requisição é independente
 * - Cacheable: respostas podem ser cacheadas
 * 
 * CONCEITO DIDÁTICO - Anotações Spring MVC:
 * - @RestController: combina @Controller e @ResponseBody
 * - @RequestMapping: define mapeamento base da URL
 * - @GetMapping: mapeia requisições GET
 * - @PostMapping: mapeia requisições POST
 * - @PutMapping: mapeia requisições PUT
 * - @DeleteMapping: mapeia requisições DELETE
 * - @PathVariable: extrai variáveis da URL
 * - @RequestParam: extrai parâmetros da query string
 * - @RequestBody: extrai dados do corpo da requisição
 * 
 * CONCEITO DIDÁTICO - OpenAPI/Swagger:
 * - Documentação automática da API
 * - @Tag: agrupa endpoints por funcionalidade
 * - @Operation: descreve a operação
 * - @Parameter: documenta parâmetros
 * - @ApiResponse: documenta respostas
 * - @Schema: define estrutura dos dados
 */
@RestController
@RequestMapping("/materiais")
@RequiredArgsConstructor
@Tag(name = "Materiais", description = "API para gerenciamento de materiais recicláveis")
public class MaterialController {

    private static final Logger log = LoggerFactory.getLogger(MaterialController.class);
    private final MaterialService materialService;
    private final MaterialRepository materialRepository;

    /**
     * CONCEITO DIDÁTICO - Endpoint de Criação (POST):
     * Cria um novo material
     * 
     * @param materialDTO dados do material
     * @return material criado
     */
    @PostMapping
    @Operation(summary = "Criar material", description = "Cria um novo material reciclável")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Material criado com sucesso",
                    content = @Content(schema = @Schema(implementation = MaterialDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Nome do material já existe")
    })
    public ResponseEntity<MaterialDTO> criarMaterial(
            @Parameter(description = "Dados do material", required = true)
            @Valid @RequestBody MaterialDTO materialDTO) {
        
        log.info("Recebida requisição para criar material: {}", materialDTO.getNome());
        
        MaterialDTO materialCriado = materialService.criarMaterial(materialDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(materialCriado);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Busca por ID (GET):
     * Busca material por ID
     * 
     * @param id ID do material
     * @return material encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar material por ID", description = "Retorna um material específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Material encontrado",
                    content = @Content(schema = @Schema(implementation = MaterialDTO.class))),
        @ApiResponse(responseCode = "404", description = "Material não encontrado")
    })
    public ResponseEntity<MaterialDTO> buscarPorId(
            @Parameter(description = "ID do material", required = true)
            @PathVariable Long id) {
        
        log.debug("Recebida requisição para buscar material com ID: {}", id);
        
        MaterialDTO material = materialService.buscarPorId(id);
        
        return ResponseEntity.ok(material);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Listagem (GET):
     * Lista todos os materiais ativos
     * 
     * @return lista de materiais ativos
     */
    @GetMapping
    @Operation(summary = "Listar materiais", description = "Retorna todos os materiais ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de materiais retornada com sucesso")
    })
    public ResponseEntity<List<MaterialDTO>> listarMateriais() {
        log.debug("Recebida requisição para listar materiais");
        
        List<MaterialDTO> materiais = materialService.listarMateriaisAtivos();
        
        return ResponseEntity.ok(materiais);
    }

    /**
     * Endpoint de teste simples
     */
    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint com Paginação (GET):
     * Lista materiais com paginação
     * 
     * @param ativo status de ativação
     * @param pageable configuração de paginação
     * @return página de materiais
     */
    @GetMapping("/paginados")
    @Operation(summary = "Listar materiais paginados", description = "Retorna materiais com paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de materiais retornada com sucesso")
    })
    public ResponseEntity<Page<MaterialDTO>> listarMateriaisPaginados(
            @Parameter(description = "Status de ativação do material")
            @RequestParam(required = false) Boolean ativo,
            @Parameter(description = "Configuração de paginação")
            @PageableDefault(size = 10) Pageable pageable) {
        
        log.debug("Recebida requisição para listar materiais paginados, ativo: {}", ativo);
        
        Page<MaterialDTO> materiais = materialService.listarMateriaisPaginados(ativo, pageable);
        
        return ResponseEntity.ok(materiais);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Busca por Categoria (GET):
     * Busca materiais por categoria
     * 
     * @param categoria categoria do material
     * @return lista de materiais da categoria
     */
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar materiais por categoria", description = "Retorna materiais de uma categoria específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materiais da categoria retornados com sucesso")
    })
    public ResponseEntity<List<MaterialDTO>> buscarPorCategoria(
            @Parameter(description = "Categoria do material", required = true)
            @PathVariable("categoria") CategoriaMaterial categoria) {
        
        log.debug("Recebida requisição para buscar materiais por categoria: {}", categoria);
        
        List<MaterialDTO> materiais = materialService.buscarPorCategoria(categoria);
        
        return ResponseEntity.ok(materiais);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Busca por Nome (GET):
     * Busca materiais por nome (busca parcial)
     * 
     * @param nome parte do nome do material
     * @return lista de materiais que contêm o nome
     */
    @GetMapping("/busca")
    @Operation(summary = "Buscar materiais por nome", description = "Retorna materiais que contêm o nome especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materiais encontrados com sucesso")
    })
    public ResponseEntity<List<MaterialDTO>> buscarPorNome(
            @Parameter(description = "Parte do nome do material", required = true)
            @RequestParam("nome") String nome) {
        
        log.debug("Recebida requisição para buscar materiais por nome: {}", nome);
        
        List<MaterialDTO> materiais = materialService.buscarPorNome(nome);
        
        return ResponseEntity.ok(materiais);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Busca por Faixa de Preço (GET):
     * Busca materiais por faixa de preço
     * 
     * @param precoMin preço mínimo
     * @param precoMax preço máximo
     * @return lista de materiais na faixa de preço
     */
    @GetMapping("/preco")
    @Operation(summary = "Buscar materiais por faixa de preço", description = "Retorna materiais em uma faixa de preço específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Materiais na faixa de preço retornados com sucesso")
    })
    public ResponseEntity<List<MaterialDTO>> buscarPorFaixaPreco(
            @Parameter(description = "Preço mínimo por kg", required = true)
            @RequestParam("precoMin") BigDecimal precoMin,
            @Parameter(description = "Preço máximo por kg", required = true)
            @RequestParam("precoMax") BigDecimal precoMax) {
        
        log.debug("Recebida requisição para buscar materiais por faixa de preço: {} - {}", precoMin, precoMax);
        
        List<MaterialDTO> materiais = materialService.buscarPorFaixaPreco(precoMin, precoMax);
        
        return ResponseEntity.ok(materiais);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Atualização (PUT):
     * Atualiza material existente
     * 
     * @param id ID do material
     * @param materialDTO dados para atualização
     * @return material atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar material", description = "Atualiza um material existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Material atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = MaterialDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Material não encontrado"),
        @ApiResponse(responseCode = "409", description = "Nome do material já existe")
    })
    public ResponseEntity<MaterialDTO> atualizarMaterial(
            @Parameter(description = "ID do material", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados para atualização", required = true)
            @Valid @RequestBody MaterialDTO materialDTO) {
        
        log.info("Recebida requisição para atualizar material com ID: {}", id);
        
        MaterialDTO materialAtualizado = materialService.atualizarMaterial(id, materialDTO);
        
        return ResponseEntity.ok(materialAtualizado);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Ativação/Desativação (PATCH):
     * Ativa ou desativa material
     * 
     * @param id ID do material
     * @param ativo status de ativação
     * @return material atualizado
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do material", description = "Ativa ou desativa um material")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
                    content = @Content(schema = @Schema(implementation = MaterialDTO.class))),
        @ApiResponse(responseCode = "404", description = "Material não encontrado")
    })
    public ResponseEntity<MaterialDTO> alterarStatus(
            @Parameter(description = "ID do material", required = true)
            @PathVariable Long id,
            @Parameter(description = "Status de ativação", required = true)
            @RequestParam("ativo") Boolean ativo) {
        
        log.info("Recebida requisição para alterar status do material com ID: {} para: {}", id, ativo);
        
        MaterialDTO materialAtualizado = materialService.alterarStatus(id, ativo);
        
        return ResponseEntity.ok(materialAtualizado);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Exclusão (DELETE):
     * Exclui material (exclusão lógica)
     * 
     * @param id ID do material
     * @return resposta sem conteúdo
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir material", description = "Exclui um material (exclusão lógica)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Material excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Material não encontrado")
    })
    public ResponseEntity<Void> excluirMaterial(
            @Parameter(description = "ID do material", required = true)
            @PathVariable Long id) {
        
        log.info("Recebida requisição para excluir material com ID: {}", id);
        
        materialService.excluirMaterial(id);
        
        return ResponseEntity.noContent().build();
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Estatísticas (GET):
     * Retorna estatísticas dos materiais
     * 
     * @return estatísticas dos materiais
     */
    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas", description = "Retorna estatísticas dos materiais")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")
    })
    public ResponseEntity<MaterialService.MaterialEstatisticasDTO> obterEstatisticas() {
        log.debug("Recebida requisição para obter estatísticas dos materiais");
        
        MaterialService.MaterialEstatisticasDTO estatisticas = materialService.calcularEstatisticas();
        
        return ResponseEntity.ok(estatisticas);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Teste:
     * Endpoint simples para debug dos materiais
     * 
     * @return lista simples de materiais
     */
    @GetMapping("/teste-simples")
    @Operation(summary = "Teste simples", description = "Endpoint de teste muito simples")
    public ResponseEntity<String> testeSimples() {
        return ResponseEntity.ok("Endpoint funcionando!");
    }

    @GetMapping("/teste-basico")
    @Operation(summary = "Teste básico", description = "Endpoint de teste básico sem DTOs")
    public ResponseEntity<Map<String, Object>> testeBasico() {
        log.info("Testando endpoint básico");
        
        try {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("status", "OK");
            resultado.put("timestamp", LocalDateTime.now());
            resultado.put("message", "Endpoint básico funcionando");
            
            // Tentar buscar dados básicos do repositório
            try {
                long totalMateriais = materialRepository.count();
                resultado.put("totalMateriais", totalMateriais);
                resultado.put("sucesso", true);
            } catch (Exception e) {
                resultado.put("erroRepositorio", e.getMessage());
                resultado.put("erroTipo", e.getClass().getSimpleName());
                resultado.put("sucesso", false);
            }
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro no teste básico", e);
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            erro.put("tipo", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    @GetMapping("/teste-material")
    @Operation(summary = "Teste material específico", description = "Endpoint de teste para um material específico")
    public ResponseEntity<Map<String, Object>> testeMaterialEspecifico() {
        log.info("Testando endpoint de material específico");
        
        try {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("status", "OK");
            resultado.put("timestamp", LocalDateTime.now());
            
            // Tentar buscar um material específico
            try {
                MaterialDTO material = materialService.buscarPorId(1L);
                resultado.put("material", material);
                resultado.put("sucesso", true);
            } catch (Exception e) {
                resultado.put("erroBusca", e.getMessage());
                resultado.put("erroTipo", e.getClass().getSimpleName());
                resultado.put("sucesso", false);
            }
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro no teste de material específico", e);
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            erro.put("tipo", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    /**
     * CONCEITO DIDÁTICO - Tratamento de Exceções:
     * Métodos para tratar exceções específicas e retornar respostas HTTP apropriadas
     */
    
    @ExceptionHandler(MaterialNaoEncontradoException.class)
    public ResponseEntity<String> handleMaterialNaoEncontrado(MaterialNaoEncontradoException e) {
        log.warn("Material não encontrado: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NomeMaterialJaExisteException.class)
    public ResponseEntity<String> handleNomeMaterialJaExiste(NomeMaterialJaExisteException e) {
        log.warn("Nome de material já existe: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
} 