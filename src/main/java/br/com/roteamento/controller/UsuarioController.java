package br.com.roteamento.controller;

import br.com.roteamento.dto.UsuarioDTO;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.service.UsuarioService;
import br.com.roteamento.exception.UsuarioNaoEncontradoException;
import br.com.roteamento.exception.EmailJaExisteException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLADOR REST - UsuarioController
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. ARQUITETURA REST:
 *    - Representational State Transfer
 *    - Stateless: Cada requisição é independente
 *    - Resource-based: Recursos são identificados por URLs
 *    - HTTP Methods: GET, POST, PUT, DELETE, PATCH
 *    - Status Codes: 200, 201, 400, 404, 500, etc.
 * 
 * 2. ANOTAÇÕES SPRING:
 *    - @RestController: Combina @Controller + @ResponseBody
 *    - @RequestMapping: Define mapeamento base da URL
 *    - @GetMapping: Mapeia requisições GET
 *    - @PostMapping: Mapeia requisições POST
 *    - @PutMapping: Mapeia requisições PUT
 *    - @DeleteMapping: Mapeia requisições DELETE
 *    - @PathVariable: Extrai parâmetros da URL
 *    - @RequestBody: Extrai dados do corpo da requisição
 *    - @Valid: Valida dados de entrada
 * 
 * 3. DOCUMENTAÇÃO OPENAPI (SWAGGER):
 *    - @Tag: Agrupa endpoints relacionados
 *    - @Operation: Descreve a operação
 *    - @Parameter: Documenta parâmetros
 *    - @ApiResponse: Documenta respostas possíveis
 *    - @Schema: Define estrutura dos dados
 * 
 * 4. TRATAMENTO DE RESPOSTAS:
 *    - ResponseEntity: Encapsula resposta HTTP
 *    - HttpStatus: Códigos de status HTTP
 *    - Headers: Metadados da resposta
 *    - Body: Dados da resposta
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuários", description = "APIs para gerenciamento de usuários")
public class UsuarioController {

    /**
     * SERVIÇO DE USUÁRIOS
     * 
     * CONCEITOS:
     * - final: Campo imutável após inicialização
     * - Injeção via construtor (recomendado)
     * - Acesso à lógica de negócio
     */
    private final UsuarioService usuarioService;

    /**
     * ENDPOINT PARA CRIAR USUÁRIO
     * 
     * CONCEITOS:
     * - @PostMapping: Mapeia requisições POST
     * - @RequestBody: Extrai dados do corpo da requisição
     * - @Valid: Valida dados de entrada
     * - ResponseEntity: Encapsula resposta HTTP
     * - HttpStatus.CREATED: Status 201 para criação
     * 
     * @param usuarioDTO Dados do usuário a ser criado
     * @return ResponseEntity com o usuário criado
     */
    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Email já existe")
    })
    public ResponseEntity<UsuarioDTO> criarUsuario(
            @Parameter(description = "Dados do usuário", required = true)
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        
        log.info("Recebida requisição para criar usuário: {}", usuarioDTO.getEmail());
        
        try {
            UsuarioDTO usuarioCriado = usuarioService.criarUsuario(usuarioDTO);
            log.info("Usuário criado com sucesso. ID: {}", usuarioCriado.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
            
        } catch (EmailJaExisteException e) {
            log.warn("Tentativa de criar usuário com email já existente: {}", usuarioDTO.getEmail());
            throw e;
        } catch (Exception e) {
            log.error("Erro ao criar usuário: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * ENDPOINT PARA BUSCAR USUÁRIO POR ID
     * 
     * CONCEITOS:
     * - @GetMapping: Mapeia requisições GET
     * - @PathVariable: Extrai parâmetro da URL
     * - ResponseEntity: Encapsula resposta HTTP
     * - HttpStatus.OK: Status 200 para sucesso
     * - HttpStatus.NOT_FOUND: Status 404 para não encontrado
     * 
     * @param id ID do usuário
     * @return ResponseEntity com o usuário encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable Long id) {
        
        log.debug("Recebida requisição para buscar usuário por ID: {}", id);
        
        try {
            UsuarioDTO usuario = usuarioService.buscarUsuarioPorId(id);
            return ResponseEntity.ok(usuario);
            
        } catch (UsuarioNaoEncontradoException e) {
            log.warn("Usuário não encontrado. ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar usuário por ID: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * ENDPOINT PARA LISTAR TODOS OS USUÁRIOS
     * 
     * CONCEITOS:
     * - @GetMapping: Mapeia requisições GET
     * - List: Coleção de usuários
     * - ResponseEntity: Encapsula resposta HTTP
     * - HttpStatus.OK: Status 200 para sucesso
     * 
     * @return ResponseEntity com lista de usuários
     */
    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna todos os usuários do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    })
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        log.debug("Recebida requisição para listar todos os usuários");
        
        try {
            List<UsuarioDTO> usuarios = usuarioService.listarTodosUsuarios();
            return ResponseEntity.ok(usuarios);
            
        } catch (Exception e) {
            log.error("Erro ao listar usuários: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * ENDPOINT PARA BUSCAR USUÁRIOS POR TIPO
     * 
     * CONCEITOS:
     * - @GetMapping: Mapeia requisições GET
     * - @RequestParam: Extrai parâmetro da query string
     * - ResponseEntity: Encapsula resposta HTTP
     * - Filtro por tipo de usuário
     * 
     * @param tipo Tipo de usuário para filtrar
     * @return ResponseEntity com lista de usuários filtrada
     */
    @GetMapping("/tipo")
    @Operation(summary = "Buscar usuários por tipo", description = "Retorna usuários filtrados por tipo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuários encontrados",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    })
    public ResponseEntity<List<UsuarioDTO>> buscarUsuariosPorTipo(
            @Parameter(description = "Tipo de usuário", required = true)
            @RequestParam Usuario.TipoUsuario tipo) {
        
        log.debug("Recebida requisição para buscar usuários por tipo: {}", tipo);
        
        try {
            List<UsuarioDTO> usuarios = usuarioService.buscarUsuariosPorTipo(tipo);
            return ResponseEntity.ok(usuarios);
            
        } catch (Exception e) {
            log.error("Erro ao buscar usuários por tipo: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * ENDPOINT PARA BUSCAR COLETORES ATIVOS
     * 
     * CONCEITOS:
     * - @GetMapping: Mapeia requisições GET
     * - ResponseEntity: Encapsula resposta HTTP
     * - Filtro específico para coletores ativos
     * 
     * @return ResponseEntity com lista de coletores ativos
     */
    @GetMapping("/coletores-ativos")
    @Operation(summary = "Buscar coletores ativos", description = "Retorna todos os coletores ativos no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Coletores ativos encontrados",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    })
    public ResponseEntity<List<UsuarioDTO>> buscarColetoresAtivos() {
        log.debug("Recebida requisição para buscar coletores ativos");
        
        try {
            List<UsuarioDTO> coletores = usuarioService.buscarColetoresAtivos();
            return ResponseEntity.ok(coletores);
            
        } catch (Exception e) {
            log.error("Erro ao buscar coletores ativos: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * ENDPOINT PARA ATUALIZAR USUÁRIO
     * 
     * CONCEITOS:
     * - @PutMapping: Mapeia requisições PUT
     * - @PathVariable: Extrai parâmetro da URL
     * - @RequestBody: Extrai dados do corpo da requisição
     * - @Valid: Valida dados de entrada
     * - ResponseEntity: Encapsula resposta HTTP
     * 
     * @param id ID do usuário a ser atualizado
     * @param usuarioDTO Novos dados do usuário
     * @return ResponseEntity com o usuário atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "409", description = "Email já existe")
    })
    public ResponseEntity<UsuarioDTO> atualizarUsuario(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable Long id,
            @Parameter(description = "Novos dados do usuário", required = true)
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        
        log.info("Recebida requisição para atualizar usuário. ID: {}", id);
        
        try {
            UsuarioDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
            log.info("Usuário atualizado com sucesso. ID: {}", id);
            
            return ResponseEntity.ok(usuarioAtualizado);
            
        } catch (UsuarioNaoEncontradoException e) {
            log.warn("Usuário não encontrado para atualização. ID: {}", id);
            throw e;
        } catch (EmailJaExisteException e) {
            log.warn("Tentativa de atualizar usuário com email já existente. ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário. ID: {}", id, e);
            throw e;
        }
    }

    /**
     * ENDPOINT PARA EXCLUIR USUÁRIO
     * 
     * CONCEITOS:
     * - @DeleteMapping: Mapeia requisições DELETE
     * - @PathVariable: Extrai parâmetro da URL
     * - ResponseEntity: Encapsula resposta HTTP
     * - HttpStatus.NO_CONTENT: Status 204 para exclusão bem-sucedida
     * 
     * @param id ID do usuário a ser excluído
     * @return ResponseEntity vazio
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário", description = "Exclui um usuário do sistema (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> excluirUsuario(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable Long id) {
        
        log.info("Recebida requisição para excluir usuário. ID: {}", id);
        
        try {
            usuarioService.excluirUsuario(id);
            log.info("Usuário excluído com sucesso. ID: {}", id);
            
            return ResponseEntity.noContent().build();
            
        } catch (UsuarioNaoEncontradoException e) {
            log.warn("Usuário não encontrado para exclusão. ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao excluir usuário. ID: {}", id, e);
            throw e;
        }
    }

    /**
     * ENDPOINT PARA ATIVAR USUÁRIO
     * 
     * CONCEITOS:
     * - @PatchMapping: Mapeia requisições PATCH
     * - @PathVariable: Extrai parâmetro da URL
     * - ResponseEntity: Encapsula resposta HTTP
     * - Operação parcial (apenas ativação)
     * 
     * @param id ID do usuário a ser ativado
     * @return ResponseEntity com o usuário ativado
     */
    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar usuário", description = "Ativa um usuário inativo no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário ativado com sucesso",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioDTO> ativarUsuario(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable Long id) {
        
        log.info("Recebida requisição para ativar usuário. ID: {}", id);
        
        try {
            UsuarioDTO usuarioAtivado = usuarioService.ativarUsuario(id);
            log.info("Usuário ativado com sucesso. ID: {}", id);
            
            return ResponseEntity.ok(usuarioAtivado);
            
        } catch (UsuarioNaoEncontradoException e) {
            log.warn("Usuário não encontrado para ativação. ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao ativar usuário. ID: {}", id, e);
            throw e;
        }
    }

    /**
     * ENDPOINT PARA BUSCAR USUÁRIOS POR NOME
     * 
     * CONCEITOS:
     * - @GetMapping: Mapeia requisições GET
     * - @RequestParam: Extrai parâmetro da query string
     * - ResponseEntity: Encapsula resposta HTTP
     * - Busca case-insensitive
     * 
     * @param nome Nome ou parte do nome para buscar
     * @return ResponseEntity com lista de usuários encontrados
     */
    @GetMapping("/busca")
    @Operation(summary = "Buscar usuários por nome", description = "Busca usuários por nome (case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuários encontrados",
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
    })
    public ResponseEntity<List<UsuarioDTO>> buscarUsuariosPorNome(
            @Parameter(description = "Nome ou parte do nome", required = true)
            @RequestParam String nome) {
        
        log.debug("Recebida requisição para buscar usuários por nome: {}", nome);
        
        try {
            List<UsuarioDTO> usuarios = usuarioService.buscarUsuariosPorNome(nome);
            return ResponseEntity.ok(usuarios);
            
        } catch (Exception e) {
            log.error("Erro ao buscar usuários por nome: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * ENDPOINT PARA VERIFICAR SE EMAIL EXISTE
     * 
     * CONCEITOS:
     * - @GetMapping: Mapeia requisições GET
     * - @RequestParam: Extrai parâmetro da query string
     * - ResponseEntity: Encapsula resposta HTTP
     * - Boolean: Tipo de retorno para valores true/false
     * 
     * @param email Email a ser verificado
     * @return ResponseEntity com boolean indicando se email existe
     */
    @GetMapping("/verificar-email")
    @Operation(summary = "Verificar email", description = "Verifica se um email já existe no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    public ResponseEntity<Boolean> verificarEmail(
            @Parameter(description = "Email a ser verificado", required = true)
            @RequestParam String email) {
        
        log.debug("Recebida requisição para verificar email: {}", email);
        
        try {
            boolean emailExiste = usuarioService.emailExiste(email);
            return ResponseEntity.ok(emailExiste);
            
        } catch (Exception e) {
            log.error("Erro ao verificar email: {}", e.getMessage(), e);
            throw e;
        }
    }
} 