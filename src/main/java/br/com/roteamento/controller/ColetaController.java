package br.com.roteamento.controller;

import br.com.roteamento.dto.ColetaDTO;
import br.com.roteamento.model.Coleta;
import br.com.roteamento.service.ColetaService;
import br.com.roteamento.exception.ColetaNaoEncontradaException;
import br.com.roteamento.exception.UsuarioNaoEncontradoException;
import br.com.roteamento.exception.MaterialNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * CONTROLLER COLETA - Classe que expõe endpoints REST para coletas
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. CAMADA DE APRESENTAÇÃO:
 *    - Recebe requisições HTTP
 *    - Valida dados de entrada
 *    - Chama serviços de negócio
 *    - Retorna respostas HTTP
 *    - Trata exceções da aplicação
 * 
 * 2. ANOTAÇÕES SPRING:
 *    - @RestController: Marca como controller REST
 *    - @RequestMapping: Define path base
 *    - @GetMapping: Endpoint GET
 *    - @PostMapping: Endpoint POST
 *    - @PutMapping: Endpoint PUT
 *    - @DeleteMapping: Endpoint DELETE
 *    - @PathVariable: Parâmetro da URL
 *    - @RequestParam: Parâmetro de query
 *    - @RequestBody: Corpo da requisição
 * 
 * 3. RESPOSTAS HTTP:
 *    - ResponseEntity: Wrapper para resposta HTTP
 *    - HttpStatus: Códigos de status HTTP
 *    - 200 OK: Sucesso
 *    - 201 Created: Recurso criado
 *    - 400 Bad Request: Dados inválidos
 *    - 404 Not Found: Recurso não encontrado
 *    - 500 Internal Server Error: Erro interno
 * 
 * 4. VALIDAÇÃO:
 *    - @Valid: Valida dados de entrada
 *    - Bean Validation: Anotações de validação
 *    - Tratamento de erros de validação
 */
@RestController
@RequestMapping("/coletas")
@RequiredArgsConstructor
@Slf4j
public class ColetaController {

    /**
     * SERVIÇO DE COLETAS
     * 
     * CONCEITOS:
     * - final: Campo imutável após inicialização
     * - Injeção via construtor (recomendado)
     * - Acesso à lógica de negócio
     */
    private final ColetaService coletaService;

    /**
     * ENDPOINT PARA CRIAR UMA NOVA COLETA
     * 
     * CONCEITOS:
     * - @PostMapping: Método HTTP POST
     * - @RequestBody: Corpo da requisição JSON
     * - @Valid: Valida dados de entrada
     * - ResponseEntity: Resposta HTTP customizada
     * - HttpStatus.CREATED: Status 201
     * 
     * @param coletaDTO Dados da coleta a ser criada
     * @return ResponseEntity com a coleta criada
     */
    @PostMapping
    public ResponseEntity<ColetaDTO> criarColeta(@Valid @RequestBody ColetaDTO coletaDTO) {
        log.info("Recebida requisição para criar coleta. Usuário: {}", coletaDTO.getUsuarioId());

        try {
            ColetaDTO coletaCriada = coletaService.criarColeta(coletaDTO);
            log.info("Coleta criada com sucesso. ID: {}", coletaCriada.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(coletaCriada);
        } catch (UsuarioNaoEncontradoException e) {
            log.error("Erro ao criar coleta: usuário não encontrado. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (MaterialNaoEncontradoException e) {
            log.error("Erro ao criar coleta: material não encontrado. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Erro interno ao criar coleta", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA BUSCAR COLETA POR ID
     * 
     * CONCEITOS:
     * - @GetMapping("/{id}"): Path com parâmetro
     * - @PathVariable: Extrai parâmetro da URL
     * - HttpStatus.OK: Status 200
     * - HttpStatus.NOT_FOUND: Status 404
     * 
     * @param id ID da coleta
     * @return ResponseEntity com a coleta encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColetaDTO> buscarColetaPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar coleta por ID: {}", id);

        try {
            ColetaDTO coleta = coletaService.buscarColetaPorId(id);
            return ResponseEntity.ok(coleta);
        } catch (ColetaNaoEncontradaException e) {
            log.warn("Coleta não encontrada. ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao buscar coleta por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA LISTAR TODAS AS COLETAS
     * 
     * CONCEITOS:
     * - @GetMapping: Método HTTP GET
     * - List<ColetaDTO>: Lista de DTOs
     * - ResponseEntity.ok(): Status 200
     * 
     * @return ResponseEntity com lista de coletas
     */
    @GetMapping
    public ResponseEntity<List<ColetaDTO>> listarTodasColetas() {
        log.info("Recebida requisição para listar todas as coletas");

        try {
            List<ColetaDTO> coletas = coletaService.listarTodasColetas();
            return ResponseEntity.ok(coletas);
        } catch (Exception e) {
            log.error("Erro interno ao listar coletas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA BUSCAR COLETAS POR USUÁRIO
     * 
     * CONCEITOS:
     * - @RequestParam: Parâmetro de query string
     * - Filtro por usuário específico
     * 
     * @param usuarioId ID do usuário
     * @return ResponseEntity com lista de coletas do usuário
     */
    @GetMapping("/usuario")
    public ResponseEntity<List<ColetaDTO>> buscarColetasPorUsuario(@RequestParam Long usuarioId) {
        log.info("Recebida requisição para buscar coletas do usuário: {}", usuarioId);

        try {
            List<ColetaDTO> coletas = coletaService.buscarColetasPorUsuario(usuarioId);
            return ResponseEntity.ok(coletas);
        } catch (Exception e) {
            log.error("Erro interno ao buscar coletas do usuário: {}", usuarioId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA BUSCAR COLETAS POR STATUS
     * 
     * CONCEITOS:
     * - @RequestParam: Parâmetro de query string
     * - Enum como parâmetro com conversor automático
     * 
     * @param status Status da coleta
     * @return ResponseEntity com lista de coletas com o status
     */
    @GetMapping("/status")
    public ResponseEntity<List<ColetaDTO>> buscarColetasPorStatus(@RequestParam Coleta.StatusColeta status) {
        log.info("Recebida requisição para buscar coletas com status: {}", status);

        try {
            List<ColetaDTO> coletas = coletaService.buscarColetasPorStatus(status);
            return ResponseEntity.ok(coletas);
        } catch (Exception e) {
            log.error("Erro interno ao buscar coletas por status: {}", status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA BUSCAR COLETAS PENDENTES
     * 
     * CONCEITOS:
     * - Endpoint específico para coletas pendentes
     * - Filtro por status específicos
     * 
     * @return ResponseEntity com lista de coletas pendentes
     */
    @GetMapping("/pendentes")
    public ResponseEntity<List<ColetaDTO>> buscarColetasPendentes() {
        log.info("Recebida requisição para buscar coletas pendentes");

        try {
            List<ColetaDTO> coletas = coletaService.buscarColetasPendentes();
            return ResponseEntity.ok(coletas);
        } catch (Exception e) {
            log.error("Erro interno ao buscar coletas pendentes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA ATUALIZAR COLETA
     * 
     * CONCEITOS:
     * - @PutMapping("/{id}"): Método HTTP PUT
     * - Atualização de recurso existente
     * - HttpStatus.OK: Status 200
     * 
     * @param id ID da coleta
     * @param coletaDTO Novos dados da coleta
     * @return ResponseEntity com a coleta atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<ColetaDTO> atualizarColeta(@PathVariable Long id, @Valid @RequestBody ColetaDTO coletaDTO) {
        log.info("Recebida requisição para atualizar coleta. ID: {}", id);

        try {
            ColetaDTO coletaAtualizada = coletaService.atualizarColeta(id, coletaDTO);
            return ResponseEntity.ok(coletaAtualizada);
        } catch (ColetaNaoEncontradaException e) {
            log.warn("Coleta não encontrada para atualização. ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao atualizar coleta. ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA EXCLUIR COLETA
     * 
     * CONCEITOS:
     * - @DeleteMapping("/{id}"): Método HTTP DELETE
     * - HttpStatus.NO_CONTENT: Status 204
     * 
     * @param id ID da coleta
     * @return ResponseEntity vazio
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirColeta(@PathVariable Long id) {
        log.info("Recebida requisição para excluir coleta. ID: {}", id);

        try {
            coletaService.excluirColeta(id);
            return ResponseEntity.noContent().build();
        } catch (ColetaNaoEncontradaException e) {
            log.warn("Coleta não encontrada para exclusão. ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao excluir coleta. ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA ACEITAR COLETA
     * 
     * CONCEITOS:
     * - @PostMapping: Método HTTP POST
     * - Endpoint específico para ação de negócio
     * - Parâmetros separados
     * 
     * @param coletaId ID da coleta
     * @param coletorId ID do coletor
     * @return ResponseEntity com a coleta aceita
     */
    @PostMapping("/{coletaId}/aceitar")
    public ResponseEntity<ColetaDTO> aceitarColeta(@PathVariable Long coletaId, @RequestParam Long coletorId) {
        log.info("Recebida requisição para aceitar coleta. Coleta ID: {}, Coletor ID: {}", coletaId, coletorId);

        try {
            ColetaDTO coletaAceita = coletaService.aceitarColeta(coletaId, coletorId);
            return ResponseEntity.ok(coletaAceita);
        } catch (ColetaNaoEncontradaException e) {
            log.warn("Coleta não encontrada para aceitação. ID: {}", coletaId);
            return ResponseEntity.notFound().build();
        } catch (UsuarioNaoEncontradoException e) {
            log.warn("Coletor não encontrado para aceitar coleta. ID: {}", coletorId);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.warn("Usuário não é um coletor. ID: {}", coletorId);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Erro interno ao aceitar coleta. Coleta ID: {}, Coletor ID: {}", coletaId, coletorId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA FINALIZAR COLETA
     * 
     * CONCEITOS:
     * - @PostMapping: Método HTTP POST
     * - Endpoint específico para ação de negócio
     * - Parâmetro de valor final
     * 
     * @param coletaId ID da coleta
     * @param valorFinal Valor final da coleta
     * @return ResponseEntity com a coleta finalizada
     */
    @PostMapping("/{coletaId}/finalizar")
    public ResponseEntity<ColetaDTO> finalizarColeta(@PathVariable Long coletaId, @RequestParam BigDecimal valorFinal) {
        log.info("Recebida requisição para finalizar coleta. Coleta ID: {}, Valor Final: {}", coletaId, valorFinal);

        try {
            ColetaDTO coletaFinalizada = coletaService.finalizarColeta(coletaId, valorFinal);
            return ResponseEntity.ok(coletaFinalizada);
        } catch (ColetaNaoEncontradaException e) {
            log.warn("Coleta não encontrada para finalização. ID: {}", coletaId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao finalizar coleta. Coleta ID: {}", coletaId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA CANCELAR COLETA
     * 
     * CONCEITOS:
     * - @PostMapping: Método HTTP POST
     * - Endpoint específico para ação de negócio
     * 
     * @param coletaId ID da coleta
     * @return ResponseEntity com a coleta cancelada
     */
    @PostMapping("/{coletaId}/cancelar")
    public ResponseEntity<ColetaDTO> cancelarColeta(@PathVariable Long coletaId) {
        log.info("Recebida requisição para cancelar coleta. Coleta ID: {}", coletaId);

        try {
            ColetaDTO coletaCancelada = coletaService.cancelarColeta(coletaId);
            return ResponseEntity.ok(coletaCancelada);
        } catch (ColetaNaoEncontradaException e) {
            log.warn("Coleta não encontrada para cancelamento. ID: {}", coletaId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao cancelar coleta. Coleta ID: {}", coletaId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA CONTAR COLETAS POR STATUS
     * 
     * CONCEITOS:
     * - @GetMapping: Método HTTP GET
     * - Enum como parâmetro com conversor automático
     * - Retorna contagem (Long)
     * 
     * @param status Status da coleta
     * @return ResponseEntity com a contagem
     */
    @GetMapping("/contar/status")
    public ResponseEntity<Long> contarColetasPorStatus(@RequestParam Coleta.StatusColeta status) {
        log.info("Recebida requisição para contar coletas com status: {}", status);

        try {
            Long contagem = coletaService.contarColetasPorStatus(status);
            return ResponseEntity.ok(contagem);
        } catch (Exception e) {
            log.error("Erro interno ao contar coletas por status: {}", status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 